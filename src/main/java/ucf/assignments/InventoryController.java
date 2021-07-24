package ucf.assignments;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import static ucf.assignments.InventoryRenderer.*;

public class InventoryController implements Initializable {
    private Inventory inventory;
    private InventoryRenderer inventoryRenderer;

    @FXML private Button importButton;
    @FXML private Button exportButton;
    @FXML private TextField searchBox;
    @FXML private VBox inventoryContents;

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
        inventoryRenderer = new InventoryRenderer(inventory, searchBox, inventoryContents);
    }

    @FXML
    private void addItem() {
        System.out.println("Adding an item");
        inventory.addItem();
        inventory.addItem();

        // should this clear the search box?
        inventoryRenderer.rerenderApp(searchBox.getText(), inventoryContents);
    }

    private void removeItem() {

    }

    private void toggleIsEditing() {

    }

    private void confirmEdit(MouseEvent event) {

    }

    private void cancelEdit() {

    }

    private void sortBySerialNumber() {

    }

    private void sortByName() {

    }

    private void sortByValue() {

    }

    private void onChangeSearch() {

    }

    @FXML
    private void importInventory() {
        System.out.println("Import requested");
    }

    @FXML
    private void exportInventory() {
        System.out.println("Export requested");
    }
}
