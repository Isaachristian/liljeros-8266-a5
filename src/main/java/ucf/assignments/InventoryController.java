/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Isaac Liljeros
 */

package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    private Inventory inventory;
    private InventoryRenderer inventoryRenderer;
    private InventoryIOHandler inventoryIOHandler;

    @FXML private Button importButton;
    @FXML private Button exportButton;
    @FXML private TextField searchBox;
    @FXML private VBox inventoryContents;
    @FXML private Label headerLabelSerialNumber;
    @FXML private Label headerLabelName;
    @FXML private Label headerLabelSerialValue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // style import/export buttons
        importButton.setText("\u2B73");
        importButton.setStyle("-fx-font-size: 20px; -fx-padding: 0px");
        exportButton.setText("\u2B71");
        exportButton.setStyle("-fx-font-size: 20px; -fx-padding: 0px");

        // create inventory
        inventory = new Inventory();

        // create renderer
        Label[] labels = {headerLabelSerialNumber, headerLabelName, headerLabelSerialValue};
        inventoryRenderer = new InventoryRenderer(inventory, searchBox, inventoryContents, labels);

        // create io handler
        inventoryIOHandler = new InventoryIOHandler();
    }

    @FXML
    private void addItem() {
        if (!inventory.isItemBeingEdited()) {
            inventory.addItem();

            // should this clear the search box?
            inventoryRenderer.rerenderApp(searchBox.getText(), inventoryContents);
        } else {
            new Alert(Alert.AlertType.ERROR, "An item is already being added/edited").show();
        }
    }

    @FXML
    private void sortBy(MouseEvent event) {
        Label source = (Label) event.getSource();
        source.getUserData();

        switch (Integer.parseInt((String) source.getUserData())) {
            case 0 -> inventory.setSortBy(SortBy.SERIALNUMBER);
            case 1 -> inventory.setSortBy(SortBy.NAME);
            case 2 -> inventory.setSortBy(SortBy.VALUE);
            default -> System.out.println("Something went wrong");
        }

        inventoryRenderer.rerenderApp(searchBox.getText(), inventoryContents);
    }

    @FXML
    private void searchBoxChanged() {
        inventoryRenderer.rerenderApp(searchBox.getText(), inventoryContents);
    }

    @FXML
    private void importInventory(MouseEvent event) {
        System.out.println("Import requested");
        Inventory loadedInventory = inventoryIOHandler.loadFromFile((Stage) ((Button) event.getSource()).getScene().getWindow());
        if (loadedInventory != null) {
            inventory.deleteAllItems();
            for (InventoryItem item : loadedInventory.getItems("")) {
                inventory.addItem(
                        item.getSerialNumber(),
                        item.getName(),
                        item.getValueAsDouble(),
                        item.getId()
                );
            }
            inventoryRenderer.rerenderApp(searchBox.getText(), inventoryContents);
        }
    }

    @FXML
    private void exportInventory(MouseEvent event) {
        if (!inventory.isItemBeingEdited()) {
            inventoryIOHandler.writeToFile(inventory, (Stage) ((Button) event.getSource()).getScene().getWindow());
        } else {
            new Alert(Alert.AlertType.ERROR, "You must finish adding/editing item before saving").show();
        }
    }
}
