package com.example.minesweeper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("BoardFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("MediaLab Minesweeper");
            stage.setScene(scene);
            stage.setHeight(650);
            stage.setWidth(650);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
