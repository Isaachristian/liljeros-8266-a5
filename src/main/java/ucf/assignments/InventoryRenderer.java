package ucf.assignments;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class InventoryRenderer {
    Inventory inventory;
    VBox inventoryContent;
    TextField searchBox;
    
    public InventoryRenderer(Inventory inventory, TextField searchBox, VBox inventoryContent) {
        this.inventory = inventory;
        this.searchBox = searchBox;
        this.inventoryContent = inventoryContent;
    }

    public  void rerenderApp(String filter, VBox inventoryContainer) {
        inventoryContainer.getChildren().clear();

        for (InventoryItem inventoryItem : inventory.getItems(filter)) {
            if (inventoryItem.getIsChanging()) {
                inventoryContainer.getChildren().add(drawEditingItem(inventoryItem));
            } else {
                inventoryContainer.getChildren().add(drawItem(inventoryItem));
            }
        }
    }

    private  GridPane drawItem(InventoryItem inventoryItem) {
        GridPane item = new GridPane();

        item.setAlignment(Pos.TOP_LEFT);

        // generate column constraints
        item.getColumnConstraints().addAll(
                generateColumnConstraint(100.0, 100.0, null),
                generateColumnConstraint( 10.0,  10.0, 10.0),
                generateColumnConstraint(350.0,  null, null),
                generateColumnConstraint( 10.0,  10.0, 10.0),
                generateColumnConstraint( 80.0,  80.0, null),
                generateColumnConstraint( 10.0,  10.0, 10.0),
                generateColumnConstraint( 30.0,  30.0, 30.0)
        );

        // generate row constraints
        item.getRowConstraints().addAll(
                generateRowConstraint(null, 34.0, 102.0)
        );

        item.add(drawLabel(inventoryItem.getSerialNumber(), 100), 0, 0);
        item.add(drawLabel(inventoryItem.getName(), null), 2, 0);
        item.add(drawLabel(inventoryItem.getFormattedValue(), 80), 4, 0);
        item.add(drawToggleEdit(inventoryItem), 6, 0);

        return item;
    }

    private  Label drawLabel(String title, Integer minWidth) {
        Label label = new Label();
        label.setText(title);
        label.getStyleClass().add("labels");
        if (minWidth != null) {
            label.setStyle("-fx-min-width: " + minWidth + "px;");
        } else {
            label.setWrapText(true);
        }

        return label;
    }

    private Button drawToggleEdit(InventoryItem inventoryItem) {
        Button button = new Button();
        button.getStyleClass().addAll("toggleEditButton", "interfaceButtons");
        button.setPrefWidth(30.0);
        button.setText("\u22EF");
        button.setTranslateX(-2);
        button.setOnMouseClicked(event -> {
            inventoryItem.setIsChanging(true);
            rerenderApp(searchBox.getText(), inventoryContent);
        });
        return button;
    }

    private  GridPane drawEditingItem(InventoryItem inventoryItem) {
        System.out.println(" Now drawing item being edited");
        GridPane editingItem = new GridPane();

        editingItem.setStyle("-fx-padding: 2px");

        // generate row constraints
        editingItem.getRowConstraints().addAll(
                generateRowConstraint(34.0, 34.0, 34.0),
                generateRowConstraint(34.0, 34.0, 34.0),
                generateRowConstraint(34.0, 34.0, 34.0)
        );

        // generate column constraints
        editingItem.getColumnConstraints().addAll(
                generateColumnConstraint(100.0, 100.0, null),
                generateColumnConstraint(10.0, 10.0, 10.0),
                generateColumnConstraint(350.0, null, null),
                generateColumnConstraint(10.0, 10.0, 10.0),
                generateColumnConstraint(80.0, 80.0, null),
                generateColumnConstraint(10.0, 10.0, 10.0),
                generateColumnConstraint(30.0, 30.0, 30.0)
        );

        // add elements to grid
        editingItem.add(drawSerialNumberEditing(inventoryItem), 0, 0, 1, 1);
        editingItem.add(drawNameEditing(inventoryItem), 2, 0, 1, 3);
        editingItem.add(drawValueEditing(inventoryItem), 4, 0, 1, 1);
        editingItem.add(drawConfirmButton(inventoryItem), 6, 0, 1, 1);
        editingItem.add(drawCancelButton(), 6, 1, 1, 1);
        editingItem.add(drawDeleteButton(), 6, 2, 1, 1);

        editingItem.setId(inventoryItem.getId().toString());

        return editingItem;
    }


    private  TextField drawSerialNumberEditing(InventoryItem inventoryItem) {
        TextField tf = new TextField();
        tf.setUserData("serialnumber");
        tf.setText(inventoryItem.getSerialNumber());
        tf.getStyleClass().add("interfaceTextInputs");
        tf.setStyle("-fx-min-height: 34px; -fx-min-width: 100px");

        return tf;
    }

    private  TextArea drawNameEditing(InventoryItem inventoryItem) {
        TextArea ta = new TextArea();
        ta.setText(inventoryItem.getName());
        ta.setWrapText(true);

        ta.getStyleClass().add("interfaceTextInputs");

        return ta;
    }

    private  TextField drawValueEditing(InventoryItem inventoryItem) {
        TextField tf = new TextField();
        tf.setText(inventoryItem.getFormattedValue());
        tf.setUserData("value");
        tf.getStyleClass().add("interfaceTextInputs");
        tf.setStyle("-fx-min-height: 34px; -fx-min-width: 80px");

        return tf;
    }

    private  Button drawConfirmButton(InventoryItem inventoryItem) {
        Button btn = new Button();
        btn.setText("\u2714");
        btn.setPrefWidth(30);
        btn.setPrefHeight(30);
        btn.getStyleClass().addAll("confirmButton", "interfaceButtons");

        btn.setOnMouseClicked(event -> {
            System.out.println("Saving Item with ID: " + inventoryItem.getId());

            // Attempt to set data
            Boolean isValid = true;
            GridPane editingItem = ((GridPane) ((Button) event.getSource()).getParent());
            for (Node node : editingItem.getChildren()) {
                if (node instanceof TextField tf) {
                    if (node.getUserData().equals("serialnumber")) {
                        try {
                            inventoryItem.setSerialNumber(tf.getText());
                        } catch (IllegalArgumentException e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                            alert.show();
                            isValid = false;
                        }
                    } else {
                        try {
                            inventoryItem.setValue(Double.parseDouble(tf.getText()));
                        } catch (IllegalArgumentException e) {
                            if (e instanceof NumberFormatException) {
                                inventoryItem.setValue(0.0);
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                                alert.show();
                                isValid = false;
                            }
                        }
                    }
                }
                if (node instanceof TextArea) {
                    try {
                        inventoryItem.setName(((TextArea) node).getText());
                    } catch (IllegalArgumentException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
                        alert.show();
                        isValid = false;
                    }
                }
            }

            // Mark as dirty to activate error messages (if needed)
            inventoryItem.markAsDirty();

            // if all valid,
            if (isValid.equals(true)) {
                inventoryItem.setIsChanging(false);
            }

            rerenderApp(searchBox.getText(), (VBox) editingItem.getParent());
        });

        return btn;
    }

    private  Button drawCancelButton() {
        Button btn = new Button();
        btn.setText("\uD83E\uDC14");
        btn.setPrefWidth(30);
        btn.setPrefHeight(30);
        btn.setStyle("-fx-font-size: 16pt; -fx-padding: 0px");
        btn.getStyleClass().addAll("cancelButton", "interfaceButtons");

        return btn;
    }

    private  Button drawDeleteButton() {
        Button btn = new Button();
        btn.setText("\u274C");
        btn.setPrefWidth(30);
        btn.setPrefHeight(30);
        btn.getStyleClass().addAll("deleteButton", "interfaceButtons");

        return btn;
    }

    private  ColumnConstraints generateColumnConstraint(double prefWidth, Double minWidth, Double maxWidth) {
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHgrow(Priority.SOMETIMES);
        cc.setPrefWidth(prefWidth);
        if (maxWidth != null) {
            cc.setMinWidth(minWidth);
        }
        if (maxWidth != null) {
            cc.setMaxWidth(maxWidth);
        }

        return cc;
    }

    private  RowConstraints generateRowConstraint(Double prefHeight, Double minHeight, Double maxHeight) {
        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.NEVER);
        if (prefHeight != null) {
            rc.setPrefHeight(prefHeight);
        }
        if (minHeight != null) {
            rc.setMinHeight(minHeight);
        }
        if (maxHeight != null) {
            rc.setMaxHeight(maxHeight);
        }

        return rc;
    }
}
