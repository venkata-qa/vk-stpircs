import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatisticsComparer {

    public static void main(String[] args) {
        try {
            // File paths for the old and new statistics JSON files
            String filePath1 = "old_statistics.json"; // Baseline
            String filePath2 = "new_statistics.json"; // New

            // Load the JSON data from the files
            JsonObject statsOld = loadJson(filePath1);
            JsonObject statsNew = loadJson(filePath2);

            // Process the data and store it in maps for total and API-level results
            List<String[]> totalSummaryResults = new ArrayList<>();
            Map<String, List<String[]>> apiResults = processStatistics(statsOld, statsNew, totalSummaryResults);

            // Output total summary and each API's results
            outputResults("Total Summary", totalSummaryResults, "total_summary_comparison.csv");
            outputApiResults(apiResults);

        } catch (Exception e) {
            System.err.println("Error in the main program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to load JSON from a file
    private static JsonObject loadJson(String filePath) throws IOException {
        Gson gson = new Gson();
        return gson.fromJson(new String(Files.readAllBytes(Paths.get(filePath))), JsonObject.class);
    }

    // Process the statistics and return a map of API results, and modify the total summary list
    private static Map<String, List<String[]>> processStatistics(JsonObject statsOld, JsonObject statsNew, List<String[]> totalSummaryResults) {
        Map<String, List<String[]>> apiResults = new HashMap<>();

        // Loop through each transaction in the old JSON
        for (Map.Entry<String, JsonElement> entry : statsOld.entrySet()) {
            String transactionName = entry.getKey();
            if (statsNew.has(transactionName)) {
                JsonObject oldStats = entry.getValue().getAsJsonObject();
                JsonObject newStats = statsNew.getAsJsonObject(transactionName);

                // Process each transaction and store the comparison results
                List<String[]> comparisonResults = compareTransaction(transactionName, oldStats, newStats);

                // Store the results in the appropriate place (total summary or individual API)
                if (transactionName.equalsIgnoreCase("Total")) {
                    totalSummaryResults.addAll(comparisonResults);
                } else {
                    apiResults.put(transactionName, comparisonResults);
                }
            } else {
                System.err.println("Warning: Transaction " + transactionName + " is missing in the new statistics file.");
            }
        }

        return apiResults;
    }

    // Method to compare the statistics for a transaction and return a list of comparison results
    private static List<String[]> compareTransaction(String transactionName, JsonObject oldStats, JsonObject newStats) {
        List<String[]> comparisonResults = new ArrayList<>();

        // Compare all relevant metrics
        compareAndStore(transactionName, oldStats, newStats, "sampleCount", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "errorCount", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "meanResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "medianResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "minResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "maxResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "pct1ResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "pct2ResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "pct3ResTime", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "throughput", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "receivedKBytesPerSec", comparisonResults);
        compareAndStore(transactionName, oldStats, newStats, "sentKBytesPerSec", comparisonResults);

        return comparisonResults;
    }

    // Helper method to compare and store metrics between two transactions
    private static void compareAndStore(String transactionName, JsonObject oldStats, JsonObject newStats, String metricKey, List<String[]> comparisonResults) {
        try {
            if (oldStats.has(metricKey) && newStats.has(metricKey)) {
                double oldValue = oldStats.get(metricKey).getAsDouble();
                double newValue = newStats.get(metricKey).getAsDouble();
                double numericDifference = newValue - oldValue;
                double percentageDiff = calculatePercentageDifference(oldValue, newValue);
                String status = determineStatus(metricKey, percentageDiff);

                comparisonResults.add(new String[]{
                        transactionName,
                        metricKey,
                        String.format("%.2f", oldValue),
                        String.format("%.2f", newValue),
                        String.format("%.2f", numericDifference),
                        String.format("%.2f%%", percentageDiff),
                        status
                });
            } else {
                System.err.println("Warning: Missing key '" + metricKey + "' for transaction '" + transactionName + "' in one of the statistics.json files.");
            }
        } catch (Exception e) {
            System.err.println("Error comparing metric: " + metricKey + " for transaction: " + transactionName);
            e.printStackTrace();
        }
    }

    // Method to output total summary or individual API results to console and CSV
    private static void outputResults(String header, List<String[]> results, String csvFileName) {
        System.out.println("==== " + header + " ====");
        printResults(results);
        writeToCSV(results, csvFileName);
    }

    // Method to handle the output of API results
    private static void outputApiResults(Map<String, List<String[]>> apiResults) {
        for (Map.Entry<String, List<String[]>> apiEntry : apiResults.entrySet()) {
            String apiName = apiEntry.getKey();
            List<String[]> apiComparisonResults = apiEntry.getValue();

            outputResults("API: " + apiName, apiComparisonResults, apiName + "_comparison.csv");
        }
    }

    // Method to print the results to the console
    private static void printResults(List<String[]> results) {
        System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", "Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status");
        for (String[] result : results) {
            System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", result[0], result[1], result[2], result[3], result[4], result[5], result[6]);
        }
    }

    // Method to write the results to a CSV file
    private static void writeToCSV(List<String[]> results, String outputFilePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            writer.writeNext(new String[]{"Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status"});
            writer.writeAll(results);
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to calculate percentage difference between two values
    private static double calculatePercentageDifference(double oldVal, double newVal) {
        if (oldVal == 0) return newVal * 100; // Avoid division by zero
        return ((newVal - oldVal) / oldVal) * 100;
    }

    // Method to determine the status (Improved/Degraded) with special handling for sampleCount, throughput, and data rates
    private static String determineStatus(String metric, double percentageDiff) {
        if (metric.equals("sampleCount") || metric.equals("throughput") ||
                metric.equals("receivedKBytesPerSec") || metric.equals("sentKBytesPerSec")) {
            return (percentageDiff >= 0) ? "Improved" : "Degraded"; // Increase is positive for these metrics
        } else {
            return (percentageDiff < 0) ? "Improved" : (percentageDiff > 0) ? "Degraded" : "No Change";
        }
    }
}
