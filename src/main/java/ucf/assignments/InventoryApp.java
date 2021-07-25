/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Isaac Liljeros
 */

package ucf.assignments;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryApp extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("InventoryApp.fxml"));
        var scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/InventoryAppStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.setMinHeight(400.0);
        stage.setMinWidth(500.0);
        stage.show();
    }
}
