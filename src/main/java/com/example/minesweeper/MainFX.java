package com.example.minesweeper;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainFX extends Application {
    static BoardController newGame;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            int[] input = {1, 10, 0, 150};

            newGame = new BoardController(input);
//            newGame.Init();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
