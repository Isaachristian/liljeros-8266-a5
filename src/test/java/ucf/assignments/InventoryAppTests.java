/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Isaac Liljeros
 */

package ucf.assignments;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryAppTests {

    /* ----------------- REQUIREMENT TO TEST -----------------
        (CANT TEST)
        - The user shall interact with the application through a Graphical User Interface

        (CAN TEST)
        - The user shall be able to store at least 100 inventory items
            - Each inventory item shall have a value representing its monetary value in US dollars
            - Each inventory item shall have a unique serial number in the format of XXXXXXXXXX where X can be either a letter or digit
            - Each inventory item shall have a name between 2 and 256 characters in length (inclusive)
        - The user shall be able to add a new inventory item
            - The application shall display an error message if the user enters an existing serial number for the new item
        - The user shall be able to remove an existing inventory item
        - The user shall be able to edit the value of an existing inventory item
        - The user shall be able to edit the serial number of an existing inventory item
            - The application shall prevent the user from duplicating the serial number
        - The user shall be able to edit the name of an existing inventory item
        - The user shall be able to sort the inventory items by value
        - The user shall be able to sort inventory items by serial number
        - The user shall be able to sort inventory items by name
        - The user shall be able to search for an inventory item by serial number
        - The user shall be able to search for an inventory item by name
        - The user shall be able to save their inventory items to a file
            T- he user shall be able to select the file format from among the following set of options: TSV (tab-separated value), HTML, JSON
                - TSV files shall shall list one inventory item per line, separate each field within an inventory item using a tab character, and end with the extension .txt
                - HTML files shall contain valid HTML and end with the extension .html
                    - The list of inventory items must appear as a table when the HTML file is rendered.
                - JSON files shall contain valid JSON and end with the extension .json
            - The user shall provide the file name and file location of the file to save
        - The user shall be able to load inventory items from a file that was previously created by the application.
            - The user shall provide the file name and file location of the file to load

     */

    @Test
    void testAdding100Items() {
        Inventory inventory = new Inventory();
        int itemsToAdd = 100;

        assertEquals(0, inventory.getItems("").size());

        for (int i = 0; i < itemsToAdd; i++) {
            inventory.addItem("XXXXXXXX" + String.format("%02d", i), "Name", 20.00, i);
        }

        assertEquals(100, inventory.getItems("").size());
    }

    @Test
    void testAddingItem() {
        Inventory inventory = new Inventory();
        inventory.addItem();
        inventory.getItems("").forEach(inventoryItem -> {
            inventoryItem.setSerialNumber("XXXXXXXXXX");
            inventoryItem.setName("this is the name");
            inventoryItem.setValue(30.0);
        });

        InventoryItem addedItem = inventory.getItems("").get(0);

        assertEquals(0, addedItem.getId());
        assertEquals("XXXXXXXXXX", addedItem.getSerialNumber());
        assertEquals("this is the name", addedItem.getName());
        assertEquals("30.00", addedItem.getValue());
        assertEquals("$30.00", addedItem.getFormattedValue());
        assertEquals(30.0, addedItem.getValueAsDouble());
        assertThrows(IllegalArgumentException.class, () -> {
            inventory.addItem("XXXXXXXXXX", "this is the name", 30.0, 1);
        });
    }

    @Test
    void testRemoveItem() {
        Inventory inventory = new Inventory();
        inventory.addItem("XXXXXXXXXX", "Name", 20.00, 0);
        assertEquals(1, inventory.getItems("").size());
        inventory.deleteItem(0);
        assertEquals(0, inventory.getItems("").size());
    }

    @Test
    void testEditSerialNumber() {
        Inventory inventory = new Inventory();
        inventory.addItem("XXXXXXXXXX", "Name", 20.00, 0);
        inventory.addItem("XXXXXXXXX1", "Name", 20.00, 1);

        assertThrows(IllegalArgumentException.class, () -> inventory.getItems("").get(0).setSerialNumber("XXX"));
        assertThrows(IllegalArgumentException.class, () -> inventory.getItems("").get(0).setSerialNumber("XXXXXXXXXXXX"));
        assertDoesNotThrow(() -> inventory.getItems("").get(0).setSerialNumber("BBBBBBBBBB"));
        assertFalse(inventory.isUniqueSerialNumber(0, "XXXXXXXXX1"));
        assertEquals("BBBBBBBBBB", inventory.getItems("").get(0).getSerialNumber());
    }

    @Test
    void testEditName() {
        String toLongName = "This is a sentence. This is a sentence. This is a sentence. This is a sentence." +
                " This is a sentence. This is a sentence. This is a sentence. This is a sentence." +
                " This is a sentence. This is a sentence. This is a sentence. This is a sentence. This is a sentence.";

        Inventory inventory = new Inventory();
        inventory.addItem("XXXXXXXXXX", "Name", 20.00, 0);

        assertThrows(IllegalArgumentException.class, () -> inventory.getItems("").get(0).setName(""));
        assertThrows(IllegalArgumentException.class, () -> inventory.getItems("").get(0).setName(toLongName));
        assertDoesNotThrow(() -> inventory.getItems("").get(0).setName("Valid name"));
        assertEquals("Valid name", inventory.getItems("").get(0).getName());
    }

    @Test
    void testEditValue() {
        Inventory inventory = new Inventory();
        inventory.addItem("XXXXXXXXXX", "Name", 20.00, 0);

        assertThrows(IllegalArgumentException.class, () -> inventory.getItems("").get(0).setValue(-10.0));
        assertDoesNotThrow(() -> inventory.getItems("").get(0).setValue(30.00));
        assertEquals(30.0, inventory.getItems("").get(0).getValueAsDouble());
        assertEquals("30.00", inventory.getItems("").get(0).getValue());
        assertEquals("$30.00", inventory.getItems("").get(0).getFormattedValue());
    }

    @Test
    void testSortBySerialNumber() {
        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        inventory.setSortBy(SortBy.SERIALNUMBER);
        assertEquals(1, inventory.getItems("").get(0).getId());
        assertEquals(0, inventory.getItems("").get(1).getId());
        assertEquals(2, inventory.getItems("").get(2).getId());
    }

    @Test
    void testSortByName() {
        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        inventory.setSortBy(SortBy.NAME);
        assertEquals(0, inventory.getItems("").get(0).getId());
        assertEquals(2, inventory.getItems("").get(1).getId());
        assertEquals(1, inventory.getItems("").get(2).getId());
    }

    @Test
    void testSortByValue() {
        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        inventory.setSortBy(SortBy.VALUE);
        assertEquals(2, inventory.getItems("").get(0).getId());
        assertEquals(1, inventory.getItems("").get(1).getId());
        assertEquals(0, inventory.getItems("").get(2).getId());
    }

    @Test // tests both search by serial number and name
    void testFilters() {
        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        // test filter by serial number
        assertEquals(1, inventory.getItems("BX").size());
        assertEquals("BXXXXXXXXX", inventory.getItems("BX").get(0).getSerialNumber());

        // test filter by name
        assertEquals(1, inventory.getItems("BAME").size());
        assertEquals("CXXXXXXXX1", inventory.getItems("Bame").get(0).getSerialNumber());
    }

    @Test
    void testGenerateTSVContents() {
        String expectedTSV = "BXXXXXXXXX\tAame\t30.00\t0\n" +
                "AXXXXXXXX1\tCame\t20.00\t1\n" +
                "CXXXXXXXX1\tBame\t10.00\t2\n";

        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        InventoryIOHandler inventoryIOHandler = new InventoryIOHandler();
        assertEquals(expectedTSV, inventoryIOHandler.generateTSV(inventory));
    }

    @Test
    void testGenerateHTMLContents() {
        String expectedTSV = "<table>\n" +
                "<tr>\n" +
                "<td>BXXXXXXXXX</td>\n" +
                "<td>Aame</td>\n" +
                "<td>30.00</td>\n" +
                "<td>0</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>AXXXXXXXX1</td>\n" +
                "<td>Came</td>\n" +
                "<td>20.00</td>\n" +
                "<td>1</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "<td>CXXXXXXXX1</td>\n" +
                "<td>Bame</td>\n" +
                "<td>10.00</td>\n" +
                "<td>2</td>\n" +
                "</tr>\n" +
                "</table>";

        Inventory inventory = new Inventory();
        inventory.addItem("BXXXXXXXXX", "Aame", 30.00, 0);
        inventory.addItem("AXXXXXXXX1", "Came", 20.00, 1);
        inventory.addItem("CXXXXXXXX1", "Bame", 10.00, 2);

        InventoryIOHandler inventoryIOHandler = new InventoryIOHandler();
        assertEquals(expectedTSV, inventoryIOHandler.generateHTML(inventory));
    }

    @Test
    void testReadTSVContents() {
        File file = new File(this.getClass().getClassLoader().getResource("testTSV.tsv").getFile());
        InventoryIOHandler inventoryIOHandler = new InventoryIOHandler();
        assertDoesNotThrow(() -> inventoryIOHandler.readTSV(file));

        Inventory inventoryFromTSV = inventoryIOHandler.readTSV(file);
        assertEquals(3, inventoryFromTSV.getItems("").size());
        assertEquals("IAYSXIY235", inventoryFromTSV.getItems("").get(0).getSerialNumber());
        assertEquals("XXX66gHNAA", inventoryFromTSV.getItems("").get(1).getSerialNumber());
        assertEquals("RASFDJAISH", inventoryFromTSV.getItems("").get(2).getSerialNumber());
    }

    @Test
    void testReadHTMLContents() {
        File file = new File(this.getClass().getClassLoader().getResource("testHTML.html").getFile());
        InventoryIOHandler inventoryIOHandler = new InventoryIOHandler();
        assertDoesNotThrow(() -> inventoryIOHandler.readHTML(file));

        Inventory inventoryFromHTML = inventoryIOHandler.readHTML(file);
        assertEquals(3, inventoryFromHTML.getItems("").size());
        assertEquals("IAYSXIY235", inventoryFromHTML.getItems("").get(0).getSerialNumber());
        assertEquals("XXX66gHNAA", inventoryFromHTML.getItems("").get(1).getSerialNumber());
        assertEquals("RASFDJAISH", inventoryFromHTML.getItems("").get(2).getSerialNumber());
    }
}

