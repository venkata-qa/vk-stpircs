import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticsComparer {

    // A helper function to calculate the percentage difference
    private static double calculatePercentageDifference(double oldVal, double newVal) {
        if (oldVal == 0) return newVal * 100; // Avoid division by zero
        return ((newVal - oldVal) / oldVal) * 100;
    }

    // A helper function to determine the status (Improved/Degraded) with special handling for sampleCount, throughput, and data rates
    private static String determineStatus(String metric, double percentageDiff) {
        // Treat increases in sampleCount, throughput, receivedKBytesPerSec, and sentKBytesPerSec as positive
        if (metric.equals("sampleCount") || metric.equals("throughput") || 
            metric.equals("receivedKBytesPerSec") || metric.equals("sentKBytesPerSec")) {
            return (percentageDiff >= 0) ? "Improved" : "Degraded"; // Increase is positive
        } else {
            return (percentageDiff < 0) ? "Improved" : (percentageDiff > 0) ? "Degraded" : "No Change";
        }
    }

    public static void main(String[] args) {
        try {
            // Set the paths for the statistics.json files
            String filePath1 = "old_statistics.json"; // Path to the first statistics.json file (Baseline)
            String filePath2 = "new_statistics.json"; // Path to the second statistics.json file (New)

            // Parse both statistics.json files into JSON objects
            Gson gson = new Gson();
            JsonObject statsOld = gson.fromJson(new String(Files.readAllBytes(Paths.get(filePath1))), JsonObject.class);
            JsonObject statsNew = gson.fromJson(new String(Files.readAllBytes(Paths.get(filePath2))), JsonObject.class);

            // Separate total summary and individual API results
            List<String[]> totalSummaryResults = new ArrayList<>();
            Map<String, List<String[]>> apiResults = new java.util.HashMap<>();

            // Loop through each transaction in the JSON (e.g., POST_1847, POST_1973, Total)
            for (Map.Entry<String, JsonElement> entry : statsOld.entrySet()) {
                String transactionName = entry.getKey();
                JsonElement oldTransactionStatsElement = entry.getValue();

                if (statsNew.has(transactionName)) {
                    JsonElement newTransactionStatsElement = statsNew.get(transactionName);

                    // Assuming each transaction is a JsonObject, cast the elements to JsonObject
                    JsonObject oldTransactionStats = oldTransactionStatsElement.getAsJsonObject();
                    JsonObject newTransactionStats = newTransactionStatsElement.getAsJsonObject();

                    // Store results in separate lists for total summary and APIs
                    List<String[]> comparisonResults = new ArrayList<>();

                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "sampleCount", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "errorCount", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "meanResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "medianResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "minResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "maxResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "pct1ResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "pct2ResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "pct3ResTime", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "throughput", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "receivedKBytesPerSec", comparisonResults);
                    compareAndStore(transactionName, oldTransactionStats, newTransactionStats, "sentKBytesPerSec", comparisonResults);

                    // Handle "Total" separately from individual APIs
                    if (transactionName.equalsIgnoreCase("Total")) {
                        totalSummaryResults.addAll(comparisonResults);
                    } else {
                        apiResults.put(transactionName, comparisonResults);
                    }
                } else {
                    System.err.println("Warning: Transaction " + transactionName + " is missing in the new statistics file.");
                }
            }

            // Print and write results for Total summary
            System.out.println("==== Total Summary ====");
            printResults(totalSummaryResults);
            writeToCSV(totalSummaryResults, "total_summary_comparison.csv");

            // Print and write results for each API
            for (Map.Entry<String, List<String[]>> apiEntry : apiResults.entrySet()) {
                String apiName = apiEntry.getKey();
                List<String[]> apiComparisonResults = apiEntry.getValue();
                
                System.out.println("\n==== API: " + apiName + " ====");
                printResults(apiComparisonResults);
                writeToCSV(apiComparisonResults, apiName + "_comparison.csv");
            }

        } catch (Exception e) {
            // Log the detailed error message
            System.err.println("Error in the main program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to compare metrics between two transactions and store the results
    private static void compareAndStore(String transactionName, JsonObject oldTransactionStats, JsonObject newTransactionStats, String metricKey, List<String[]> comparisonResults) {
        try {
            if (oldTransactionStats.has(metricKey) && newTransactionStats.has(metricKey)) {
                double oldValue = oldTransactionStats.get(metricKey).getAsDouble();
                double newValue = newTransactionStats.get(metricKey).getAsDouble();
                double numericDifference = newValue - oldValue;
                double percentageDiff = calculatePercentageDifference(oldValue, newValue);
                String status = determineStatus(metricKey, percentageDiff);

                String[] resultRow = {
                    transactionName,
                    metricKey,
                    String.format("%.2f", oldValue),
                    String.format("%.2f", newValue),
                    String.format("%.2f", numericDifference),
                    String.format("%.2f%%", percentageDiff),
                    status
                };

                comparisonResults.add(resultRow);
            } else {
                // Handle cases where the key might not be present in one of the JSON files
                System.err.println("Warning: Missing key '" + metricKey + "' for transaction '" + transactionName + "' in one of the statistics.json files.");
            }
        } catch (Exception e) {
            System.err.println("Error comparing metric: " + metricKey + " for transaction: " + transactionName);
            e.printStackTrace();
        }
    }

    // Method to print results in the console
    private static void printResults(List<String[]> results) {
        System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", "Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status");
        for (String[] result : results) {
            System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", result[0], result[1], result[2], result[3], result[4], result[5], result[6]);
        }
    }

    // Method to write the comparison results into a CSV file
    private static void writeToCSV(List<String[]> comparisonResults, String outputFilePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            // Add header
            writer.writeNext(new String[]{"Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status"});
            
            // Add data
            writer.writeAll(comparisonResults);
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
