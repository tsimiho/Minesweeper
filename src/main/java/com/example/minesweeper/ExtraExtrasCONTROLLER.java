package com.example.minesweeper;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ExtraExtrasCONTROLLER {

    @FXML
    private AnchorPane ExtraExtrasPIC;

    @FXML
    private Button BackMainMenu;

    @FXML
    void MainMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(); // Create loading object
        loader.setLocation(getClass().getResource("MainMenuFXML.fxml")); // fxml location
        VBox root = loader.load(); // Load layout
        Scene scene = new Scene(root); // Create scene with chosen layout
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        primaryStage.setTitle("BOOM Beach"); // Set stage's title
//      primaryStage.setMinWidth(400); // Won't be allowed to make width/height smaller
//      primaryStage.setMinHeight(350);
//      primaryStage.setMaxWidth(600);
//      primaryStage.setMaxHeight(450);
        primaryStage.setScene(scene); // Set scene to stage
        primaryStage.show(); // Show stage
    }
}