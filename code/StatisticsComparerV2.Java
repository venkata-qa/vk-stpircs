package com.myproject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StatisticsComparer {

    public static void main(String[] args) {
        try {
            // Load the baseline statistics.json file from the resources folder (classpath)
            InputStream baselineStream = StatisticsComparer.class.getClassLoader().getResourceAsStream("baseline_statistics.json");
            if (baselineStream == null) {
                throw new RuntimeException("Baseline statistics file not found in classpath");
            }

            Gson gson = new Gson();
            JsonObject statsOld = gson.fromJson(new String(baselineStream.readAllBytes()), JsonObject.class);

            // Load the new statistics.json file from target/jmeter/reports/statistics.json
            String newFilePath = "target/jmeter/reports/statistics.json";
            JsonObject statsNew = gson.fromJson(new String(Files.readAllBytes(Paths.get(newFilePath))), JsonObject.class);

            // Separate total summary and individual API results
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

    // Method to load JSON from a file (used for the baseline JSON from classpath and target JSON from filesystem)
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

    // Helper methods for printing and writing results to CSV are the same as in the previous version
    // ...
}
