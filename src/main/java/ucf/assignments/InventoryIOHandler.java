/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Isaac Liljeros
 */

package ucf.assignments;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class InventoryIOHandler {

    public void writeToFile(Inventory inventory, Stage stage) {
        // get file location and type from the user
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "TSV",  "*.tsv"),
                new FileChooser.ExtensionFilter("HTML", "*.html")
        );
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            System.out.println(file.getName());

            // generate appropriate file
            String[] fileParts =  file.getName().split("\\.");
            String extension = fileParts[fileParts.length - 1];

            try {
                if (file.createNewFile()) {
                    var fw = new FileWriter(file.getAbsolutePath());

                    switch (extension) {
                        case "tsv"  -> fw.write(generateTSV(inventory));
                        case "html" -> fw.write(generateHTML(inventory));
                    }

                    fw.close();
                } else {
                    new Alert(Alert.AlertType.ERROR, "This file already exists").show();
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong saving the file").show();
            }

            // save to location

        } else {
            System.out.println("operation was canceled");
        }
    }

    public String generateTSV(Inventory inventory) {
        List<InventoryItem> items = inventory.getItems("");

        StringBuilder fileContents = new StringBuilder();

        for (InventoryItem item : items) {
            fileContents.append(String.format("%s\t%s\t%s\t%s\n", item.getSerialNumber(), item.getName(), item.getValue(), item.getId()));
        }

        return fileContents.toString();
    }

    public String generateHTML(Inventory inventory) {
        List<InventoryItem> items = inventory.getItems("");

        StringBuilder fileContents = new StringBuilder();

        fileContents.append("<table>\n");

        for (InventoryItem item : items) {
            fileContents.append(String.format("""
                                    <tr>
                                    <td>%s</td>
                                    <td>%s</td>
                                    <td>%s</td>
                                    <td>%s</td>
                                    </tr>
                                    """,
                    item.getSerialNumber(), item.getName(), item.getValue(), item.getId()));
        }

        fileContents.append("</table>");

        return fileContents.toString();
    }

    public Inventory loadFromFile(Stage stage) {
        Inventory inventory = null;

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter( "TSV",  "*.tsv"),
                new FileChooser.ExtensionFilter("HTML", "*.html")
        );

        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String[] fileNameParts = file.getName().split("\\.");
            String extension = fileNameParts[fileNameParts.length - 1];

            switch (extension) {
                case "tsv" -> inventory = readTSV(file);
                case "html" -> inventory = readHTML(file);
                default -> System.out.println("something went wrong");
            }
        }
        return inventory;
    }

    public Inventory readTSV(File file) {
        Inventory inventory = new Inventory();
        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] lineTokens = scanner.nextLine().split("\\t");
                inventory.addItem(
                        lineTokens[0],
                        lineTokens[1],
                        Double.parseDouble(lineTokens[2]),
                        Integer.parseInt(lineTokens[3])
                );
            }

            scanner.close();

            if (inventory.getItems("").size() > 0) {
                return inventory;
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open file").show();
        }

        return null;
    }

    public Inventory readHTML(File file) {
        Inventory inventory = new Inventory();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);

            // ignore <table>
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("<tr>")) {
                    // scan next 4 lines
                    inventory.addItem(
                            parseHTMLLine(scanner.nextLine()),
                            parseHTMLLine(scanner.nextLine()),
                            parseHTMLLineAsDouble(scanner.nextLine()),
                            parseHTMLLineAsInteger(scanner.nextLine())
                    );

                    // ignore </br>
                    scanner.nextLine();
                }
            }

            scanner.close();

            if (inventory.getItems("").size() > 0) {
                return inventory;
            }
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open file").show();
        } catch (NumberFormatException e) {
            if (scanner != null) {
                scanner.close();
            }
            new Alert(Alert.AlertType.ERROR, "File contents in invalid format").show();
        }

        return null;
    }

    private String parseHTMLLine(String line) {
        return line.replaceAll("^<td>|</td>$", "");
    }

    private Double parseHTMLLineAsDouble(String line) throws NumberFormatException {
        return Double.parseDouble(line.replaceAll("^<td>|</td>$", ""));
    }

    private Integer parseHTMLLineAsInteger(String line) throws NumberFormatException {
        return Integer.parseInt(line.replaceAll("^<td>|</td>$", ""));
    }
}
