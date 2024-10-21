package uk.gov.hmrc.eis.ipaas.migration.testing;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JmeterStatisticsComparer {

    // Add metric definitions (human-readable names and descriptions)
    private static final Map<String, String> metricDefinitions = new LinkedHashMap<>();

    static {
        // Use LinkedHashMap to maintain the order of the metrics
        metricDefinitions.put("sampleCount", "Sample Count - The total number of requests. A higher number indicates more data collected and more reliable metrics.");
        metricDefinitions.put("errorCount", "Error Count - The number of failed requests. A lower number is better, indicating fewer errors.");
        metricDefinitions.put("meanResTime", "Mean Response Time (ms) - The average response time. A lower value is better as it indicates faster response.");
        metricDefinitions.put("medianResTime", "Median Response Time (ms) - The middle response time in the sorted data set. A lower value is better.");
        metricDefinitions.put("minResTime", "Min Response Time (ms) - The fastest response time recorded. A lower value is better.");
        metricDefinitions.put("maxResTime", "Max Response Time (ms) - The slowest response time recorded. A lower value is better.");
        metricDefinitions.put("pct1ResTime", "90th Percentile Response Time (ms) - 90% of the responses are faster than this time. A lower value is better.");
        metricDefinitions.put("pct2ResTime", "95th Percentile Response Time (ms) - 95% of the responses are faster than this time. A lower value is better.");
        metricDefinitions.put("pct3ResTime", "99th Percentile Response Time (ms) - 99% of the responses are faster than this time. A lower value is better.");
        metricDefinitions.put("throughput", "Throughput (req/s) - The number of requests processed per second. A higher value is better, indicating more requests handled.");
        metricDefinitions.put("receivedKBytesPerSec", "Received KBytes per Second - The amount of data received per second. Can indicate load on the server, context dependent.");
        metricDefinitions.put("sentKBytesPerSec", "Sent KBytes per Second - The amount of data sent per second. Can indicate load on the server, context dependent.");
    }

    public static void runCompareStatistics(String baselinePath, String targetPath, String grp, String testType) {
        try {
            // Read the baseline statistics.json file
            String baselineContent = new String(Files.readAllBytes(Paths.get(baselinePath)));
            String targetContent = new String(Files.readAllBytes(Paths.get(targetPath)));

            // Parse the JSON files
            Gson gson = new Gson();
            JsonObject statsOld = gson.fromJson(baselineContent, JsonObject.class);
            JsonObject statsNew = gson.fromJson(targetContent, JsonObject.class);

            // Separate total summary and individual API results
            List<String[]> totalSummaryResults = new ArrayList<>();
            Map<String, List<String[]>> apiResults = processStatistics(statsOld, statsNew, totalSummaryResults);

            // Output total summary and each API's results
            outputResults("Total Summary", totalSummaryResults, "total_summary_comparison.csv", grp, testType);
            outputApiResults(apiResults, grp, testType);

        } catch (Exception e) {
            System.err.println("Error in the main program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to process statistics and return a map of API results, and modify the total summary list
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

        // Compare all relevant metrics in the fixed order defined by metricDefinitions
        for (String metricKey : metricDefinitions.keySet()) {
            compareAndStore(transactionName, oldStats, newStats, metricKey, comparisonResults);
        }

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
                        metricKey, // Only the metric name goes here
                        String.format("%.2f", oldValue),
                        String.format("%.2f", newValue),
                        String.format("%.2f", numericDifference),
                        String.format("%.2f%%", percentageDiff),
                        status,
                        metricDefinitions.get(metricKey) // Add metric description as the last column
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
    private static void outputResults(String header, List<String[]> results, String csvFileName, String grp, String testType) {
        System.out.println("==== " + header + " ====");
        printResults(results);
        printConfluenceTable(results); // Print in Confluence format
        writeToCSV(results, csvFileName, grp, testType);
    }

    // Method to handle the output of API results
    private static void outputApiResults(Map<String, List<String[]>> apiResults, String grp, String testType) {
        for (Map.Entry<String, List<String[]>> apiEntry : apiResults.entrySet()) {
            String apiName = apiEntry.getKey();
            List<String[]> apiComparisonResults = apiEntry.getValue();

            outputResults("API: " + apiName, apiComparisonResults, apiName + "_comparison.csv", grp, testType);
        }
    }

    // Method to print the results to the console
    private static void printResults(List<String[]> results) {
        System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s%-60s\n", "Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status", "Definition");
        for (String[] result : results) {
            System.out.printf("%-25s%-35s%-12s%-12s%-12s%-12s%-12s%-60s\n", result[0], result[1], result[2], result[3], result[4], result[5], result[6], result[7]);
        }
    }

    // Method to print the results in Confluence table format
    private static void printConfluenceTable(List<String[]> results) {
        // Print header in Confluence format
        System.out.println("|| Transaction || Metric || Baseline || New || Difference || Percent Diff || Status || Definition ||");
        for (String[] result : results) {
            System.out.printf("| %s | %s | %s | %s | %s | %s | %s | %s |\n", result[0], result[1], result[2], result[3], result[4], result[5], result[6], result[7]);
        }
    }

    // Method to write the results to a CSV file in a specific directory with dynamic "grp" and "testType"
    private static void writeToCSV(List<String[]> results, String fileName, String grp, String testType) {
        // Define the dynamic path for the CSV output directory
        String outputDirectory = "src/test/resources/compare-results/" + grp + "/" + testType;

        // Create the directories if they don't exist
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Full file path including directory
        String outputFilePath = outputDirectory + "/" + fileName;

        // Write the CSV file
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            writer.writeNext(new String[]{"Transaction", "Metric", "Baseline", "New", "Difference", "Percent Diff", "Status", "Definition"});
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
