package com.example.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class MainFX extends Application {
    static int onOoff = 0;
    static int check = 0, check2 = 0;

    public MenuBar menubar;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(); // Create loading object
            loader.setLocation(getClass().getResource("MainMenuFXML.fxml")); // fxml location
            VBox root = loader.load(); // Load layout
            Scene scene = new Scene(root); // Create scene with chosen layout

            primaryStage.setTitle("BOOM Beach"); // Set stage's title

//          primaryStage.setMinWidth(400); // Won't be allowed to make width/height smaller
//          primaryStage.setMinHeight(350);
//          primaryStage.setMaxWidth(600); // It will put constraints on the new scenes!!
//          primaryStage.setMaxHeight(450);
            primaryStage.setScene(scene); // Set scene to stage
            primaryStage.show(); // Show stage
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
