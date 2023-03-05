package com.example.minesweeper;

import java.io.IOException;
import java.util.*;
import java.lang.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class NewGameController {

    public static int level, mines, hypermine, time; // To access them even when resetting board
    static Mines game;
    //    static int mouseClick = -1;
    public int numberOfFlags;
    GridPane board;
    BoardController bc = new BoardController();
    Button b;
//    @FXML
//    private TextField NumRows;
//    @FXML
//    private TextField NumCols;
//    @FXML
//    private TextField NumM;
//    @FXML
//    private Button StartGame;
//    @FXML
//    private Button BackMainMenu;
//    @FXML
//    private Button RandomGame;
//    @FXML
//    private Button Music;

    public NewGameController(int[] input) {
        level = input[0];
        mines = input[1];
        hypermine = input[2];
        time = input[3];
    }

    String GetImage(String ImageUrl) {
        return Objects.requireNonNull(getClass().getResource(ImageUrl)).toExternalForm();
    }

    void StartGame() throws IOException {
        int rows = 0, columns = 0;
        System.out.println(level);
        if (level == 1) {
            rows = 9;
            columns = 9;
        } else if (level == 2) {
            rows = 16;
            columns = 16;
        }

        game = new Mines(rows, columns, mines, hypermine);

//        FXMLLoader loader = new FXMLLoader(); // Create loading object
//        loader.setLocation(getClass().getResource("BoardFXML.fxml")); // fxml location
//
//        AnchorPane root = loader.load(); // Load layout
//        Scene scene = new Scene(root); // Create scene with chosen layout
////        Stage gameStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        Stage gameStage = new Stage();
//
//        gameStage.setTitle("MediaLab Minesweeper"); // Set stage's title
//
//        gameStage.setScene(scene); // Set scene to stage
//
//        bc = loader.getController(); // Prepare board in BoardCONTROLLER
//
//        bc.winLose.setVisible(false);

        board = new GridPane();


        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                b = new Button(" ");
                b.setMinSize(30, 30);
                b.setMaxSize(30, 30);
                b.setStyle("-fx-font-size:11");
                board.add(b, i, j);
                board.setMargin(b, new Insets(5, 5, 5, 5));
                board.setHalignment(b, HPos.CENTER);
                board.setValignment(b, VPos.CENTER);
            }
        }
        for (int i = 0; i < board.getChildren().size(); i++) {
            ((ButtonBase) board.getChildren().get(i)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int x, y;
                    y = (int) ((Button) event.getSource()).getProperties().get("gridpane-column");
                    x = (int) ((Button) event.getSource()).getProperties().get("gridpane-row");
                    if (event.getButton().equals(MouseButton.PRIMARY))
                        game.open(x, y, true);
                    else if (event.getButton().equals(MouseButton.SECONDARY))
                        game.toggleFlag(x, y);
                    for (Node child : board.getChildren()) {
                        int j = (int) ((Button) child).getProperties().get("gridpane-column");
                        int i = (int) ((Button) child).getProperties().get("gridpane-row");
                        if (game.board[i][j].charAt(1) == 'F' && game.board[i][j].charAt(0) == 'D')
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/flag.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        if (game.board[i][j].charAt(1) == 'F' && game.board[i][j].charAt(0) == 'N')
                            ((Button) child).setStyle("-fx-font-size:11");
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'E') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/exposed.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                            ((Button) child).setDisable(true);
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'I') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/inactive.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'M') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/mine.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'H') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/hypermine.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'B') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/hitmine.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '1') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number1.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '2') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number2.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '3') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number3.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '4') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number4.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '5') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number5.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '6') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number6.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '7') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number7.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == '8') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/number8.png") + "'); " +
                                    "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
                        }
                        if (Mines.winLose == 1) {
                            game.setShowAll();
//                            bc.winLose.setVisible(true);
//                            bc.winLose.setText("YOU WIN!");
                        }
                        if (Mines.winLose == 0) {
                            game.setShowAll();
//                            bc.winLose.setVisible(true);
//                            bc.winLose.setText("YOU LOSE! Try again...");
                        }
                        Mines.winLose = -1;
                        numberOfFlags = game.getNumberOfFlags();
//                        bc.SetFlagCount(numberOfFlags);
                    }
                }
            });
        }
    }

}
