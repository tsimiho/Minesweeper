package com.example.minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {
    static int onOoff = 0;
    static int check = 0, check2 = 0;

    static int rows, columns, mines, level;

    static Boolean hyperMine;
    static Mines game;

    static NewGameController newGame;
    static FileController fc = new FileController();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
//            FXMLLoader loader = new FXMLLoader(); // Create loading object
//            loader.setLocation(getClass().getResource("MainMenuFXML.fxml")); // fxml location
//            VBox root = loader.load(); // Load layout
//            Scene scene = new Scene(root); // Create scene with chosen layout
//
//            primaryStage.setTitle("BOOM Beach"); // Set stage's title
//
////          primaryStage.setMinWidth(400); // Won't be allowed to make width/height smaller
////          primaryStage.setMinHeight(350);
////          primaryStage.setMaxWidth(600); // It will put constraints on the new scenes!!
////          primaryStage.setMaxHeight(450);
//            primaryStage.setScene(scene); // Set scene to stage
//            primaryStage.show(); // Show stage
            fc.getGameDescription();

            newGame = new NewGameController(10, 10, 10);
            newGame.StartGame();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
