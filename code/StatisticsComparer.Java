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

public class StatisticsComparer {

    // A simple helper function to calculate the difference and determine status (Improved/Degraded)
    private static String calculateDifferenceAndStatus(double oldVal, double newVal) {
        double diff = newVal - oldVal;
        if (diff < 0) {
            return String.format("%.2f  Improved", diff);
        } else if (diff > 0) {
            return String.format("%.2f  Degraded", diff);
        } else {
            return String.format("%.2f  No Change", diff);
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath1 = "old_statistics.json"; // Path to the first statistics.json file (Baseline)
        String filePath2 = "new_statistics.json"; // Path to the second statistics.json file (New)

        // Parse both statistics.json files into JSON objects
        Gson gson = new Gson();
        JsonObject statsOld = gson.fromJson(new String(Files.readAllBytes(Paths.get(filePath1))), JsonObject.class);
        JsonObject statsNew = gson.fromJson(new String(Files.readAllBytes(Paths.get(filePath2))), JsonObject.class);

        // Create a list to hold comparison results for each metric
        List<String[]> comparisonResults = new ArrayList<>();

        // Compare common metrics and store the results in comparisonResults list
        compareAndStore(statsOld, statsNew, "Average Response Time (ms)", "meanResTime", comparisonResults);
        compareAndStore(statsOld, statsNew, "Max Response Time (ms)", "maxResTime", comparisonResults);
        compareAndStore(statsOld, statsNew, "95th Percentile Response Time (ms)", "pct1ResTime", comparisonResults);
        compareAndStore(statsOld, statsNew, "99th Percentile Response Time (ms)", "pct2ResTime", comparisonResults);
        compareAndStore(statsOld, statsNew, "Throughput (req/s)", "throughput", comparisonResults);
        compareAndStore(statsOld, statsNew, "Error Rate (%)", "errorCount", comparisonResults);
        compareAndStore(statsOld, statsNew, "Latency (ms)", "latency", comparisonResults);

        // Print the output in a tabular form
        System.out.printf("%-35s%-12s%-12s%-12s%-12s\n", "Metric", "Baseline", "New", "Difference", "Status");
        for (String[] result : comparisonResults) {
            System.out.printf("%-35s%-12s%-12s%-12s%-12s\n", result[0], result[1], result[2], result[3], result[4]);
        }

        // Write the results to CSV
        writeToCSV(comparisonResults, "comparison_report.csv");

        // Write the results to Excel
        writeToExcel(comparisonResults, "comparison_report.xlsx");
    }

    // Helper method to compare metrics between two statistics files and store the results
    private static void compareAndStore(JsonObject statsOld, JsonObject statsNew, String metricName, String jsonKey, List<String[]> comparisonResults) {
        double oldValue = statsOld.get(jsonKey).getAsDouble();
        double newValue = statsNew.get(jsonKey).getAsDouble();
        String diffStatus = calculateDifferenceAndStatus(oldValue, newValue);
        String[] resultRow = {metricName, String.format("%.2f", oldValue), String.format("%.2f", newValue), diffStatus.split(" ")[0], diffStatus.split(" ")[1]};
        comparisonResults.add(resultRow);
    }

    // Method to write the comparison results into a CSV file
    private static void writeToCSV(List<String[]> comparisonResults, String outputFilePath) throws IOException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(outputFilePath))) {
            // Add header
            writer.writeNext(new String[]{"Metric", "Baseline", "New", "Difference", "Status"});
            
            // Add data
            writer.writeAll(comparisonResults);
        }
    }

    // Method to write the comparison results into an Excel file
    private static void writeToExcel(List<String[]> comparisonResults, String outputFilePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Comparison");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Metric");
        headerRow.createCell(1).setCellValue("Baseline");
        headerRow.createCell(2).setCellValue("New");
        headerRow.createCell(3).setCellValue("Difference");
        headerRow.createCell(4).setCellValue("Status");

        // Fill data rows
        int rowIdx = 1;
        for (String[] result : comparisonResults) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(result[0]);
            row.createCell(1).setCellValue(result[1]);
            row.createCell(2).setCellValue(result[2]);
            row.createCell(3).setCellValue(result[3]);
            row.createCell(4).setCellValue(result[4]);
        }

        // Write the output to an Excel file
        try (FileOutputStream fileOut = new FileOutputStream(outputFilePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
    }
}




// <dependencies>
//     <dependency>
//         <groupId>com.google.code.gson</groupId>
//         <artifactId>gson</artifactId>
//         <version>2.8.8</version>
//     </dependency>
//     <dependency>
//         <groupId>com.opencsv</groupId>
//         <artifactId>opencsv</artifactId>
//         <version>5.5.2</version>
//     </dependency>
//     <dependency>
//         <groupId>org.apache.poi</groupId>
//         <artifactId>poi-ooxml</artifactId>
//         <version>5.2.2</version>
//     </dependency>
// </dependencies>
