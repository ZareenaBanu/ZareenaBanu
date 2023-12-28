package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

//To Curate exported files and delete the unnecessary colomns and make the files ready for uploading to SMS in a folder
public class CurateCSVFilesToUploadInSMS {

    public static void main(String[] args) {
        // Specify the input folder containing CSV files
        String inputExportedListFolder = "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\SMS\\ExportedList\\2023Dec28";

        // Specify the output folder for modified CSV files
        String outputCuratedListFolder =  "C:\\Users\\P128DEF\\OneDrive - Ceridian HCM Inc\\MyFolder\\Zar-Per\\TJ\\SMS\\curatedCSVFilesToUploadInSMS";

        // Call the recursive method to process CSV files
        processCSVFiles(inputExportedListFolder, outputCuratedListFolder);
    }

    public static void processCSVFiles(String inputExportedListFolder, String outputCuratedListFolder) {
        File inputDir = new File(inputExportedListFolder);

        // Check if the provided path is a directory
        if (inputDir.isDirectory()) {
            // Get list of files and subdirectories in the input folder
            File[] files = inputDir.listFiles();

            if (files != null) {
                // Iterate through files and directories
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                        // Process only CSV files
                        processSingleCSVFile(file, outputCuratedListFolder);
                    } else if (file.isDirectory()) {
                        // Recursively process files in subdirectories
                        processCSVFiles(file.getAbsolutePath(), outputCuratedListFolder);
                    }
                }
            }
        } else {
            System.out.println("Invalid input folder path.");
        }
    }

    public static void processSingleCSVFile(File inputFile, String outputCuratedListFolder) {
        try {
            // Read lines from the input CSV file
            List<String> lines = Files.readAllLines(inputFile.toPath());

            // Create a new StringBuilder to store modified CSV content
            StringBuilder modifiedContent = new StringBuilder();

            // Remove columns 1 to 4 and columns 12 to 17
            for (String line : lines) {
                String[] columns = line.split(",");
                if (columns.length >= 17) {
                    // Copy columns 5 to 11
                    modifiedContent.append(String.join(",", Arrays.copyOfRange(columns, 4, 11)))
                            .append(",");
                    // Copy columns 18 onwards
                    modifiedContent.append(String.join(",", Arrays.copyOfRange(columns, 17, columns.length)))
                            .append("\n");
                }
            }

            // Create the output folder if it doesn't exist
            Files.createDirectories(Paths.get(outputCuratedListFolder));

            // Define the output file path and name
            String outputFileName = "OldSMSGroups - " + inputFile.getName();
            Path outputPath = Paths.get(outputCuratedListFolder, outputFileName);

            // Write the modified content to the output file
            Files.write(outputPath, modifiedContent.toString().getBytes());

            System.out.println("Processed: " + inputFile.getName() + " -> " + outputFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

