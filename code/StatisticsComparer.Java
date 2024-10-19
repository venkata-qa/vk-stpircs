import com.google.gson.Gson;
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

    // A helper function to determine the status (Improved/Degraded) with special handling for sampleCount
    private static String determineStatus(String metric, double percentageDiff) {
        if (metric.equals("sampleCount")) {
            return (percentageDiff >= 0) ? "Improved" : "Degraded"; // Sample count increase is always positive
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

            // Create a list to hold comparison results for each metric
            List<String[]> comparisonResults = new ArrayList<>();

            // Loop through each transaction in the JSON (e.g., POST_1847, POST_1973)
            for (Map.Entry<String, JsonObject> entry : statsOld.entrySet()) {
                String transactionName = entry.getKey();
                
                if (statsNew.has(transactionName)) {
                    JsonObject oldTransactionStats = entry.getValue();
                    JsonObject newTransactionStats = statsNew.getAsJsonObject(transactionName);

                    // Compare the common metrics for each transaction and store the results
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
                } else {
                    System.err.println("Warning: Transaction " + transactionName + " is missing in the new statistics file.");
                }
            }

            // Print the output in a tabular form
            System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", "Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status");
            for (String[] result : comparisonResults) {
                System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s\n", result[0], result[1], result[2], result[3], result[4], result[5], result[6]);
            }

            // Write the results to CSV
            writeToCSV(comparisonResults, "comparison_report.csv");

            // Write the results to Excel
            writeToExcel(comparisonResults, "comparison_report.xlsx");

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
                String status = determineStatus(metricKey, percentageDiff);  // Adjusted status logic for sampleCount

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

    // Method to write the comparison results into an Excel file
    private static void writeToExcel(List<String[]> comparisonResults, String outputFilePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Comparison");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Transaction");
            headerRow.createCell(1).setCellValue("Metric");
            headerRow.createCell(2).setCellValue("Baseline");
            headerRow.createCell(3).setCellValue("New");
            headerRow.createCell(4).setCellValue("Difference");
            headerRow.createCell(5).setCellValue("Percent Diff");
            headerRow.createCell(6).setCellValue("Status");

            // Fill data rows
            int rowIdx = 1;
            for (String[] result : comparisonResults) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(result[0]);
                row.createCell(1).setCellValue(result[1]);
                row.createCell(2).setCellValue(result[2]);
                row.createCell(3).setCellValue(result[3]);
                row.createCell(4).setCellValue(result[4]);
                row.createCell(5).setCellValue(result[5]);
                row.createCell(6).setCellValue(result[6]);
            }

            // Write the output to an Excel file
            try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            System.err.println("Error writing Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
