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
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.util.Pair;
import org.controlsfx.control.PopOver;
import javafx.scene.control.ButtonBar.ButtonData;


import java.util.Optional;

public class BoardController {
    public int numberOfFlags;

    public FileController fc = new FileController();

    public int scenario_id;
    @FXML
    public GridPane TheBoard;
    @FXML
    public Label winLose;
    Button b;
    @FXML
    private Button ResetBoard;

    @FXML
    void ResetBoard(ActionEvent event) throws IOException {
        NewGameController.game = new Mines(NewGameController.rows, NewGameController.columns, NewGameController.mines);

        FXMLLoader loader = new FXMLLoader(); // Create loading object
        loader.setLocation(getClass().getResource("BoardFXML.fxml")); // fxml location

        AnchorPane root = loader.load(); // Load layout
        Scene scene = new Scene(root); // Create scene with chosen layout
        Stage gameStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        gameStage.setTitle("BOOM Beach"); // Set stage's title
//      gameStage.setMinWidth(1000); // Won't be allowed to make width/height smaller
//      gameStage.setMinHeight(500);
//      gameStage.setMaxWidth(2000);
//      gameStage.setMaxHeight(1000);
        gameStage.setScene(scene); // Set scene to stage

        BoardController bCont = loader.getController(); // Prepare board in BoardCONTROLLER

        bCont.winLose.setVisible(false);

        for (int i = 0; i < NewGameController.columns; i++) {
            for (int j = 0; j < NewGameController.rows; j++) {
                b = new Button(" ");
                b.setMinSize(30, 30);
                b.setMaxSize(30, 30);
                b.setStyle("-fx-font-size:11");
                bCont.TheBoard.add(b, i, j);
                GridPane.setMargin(b, new Insets(5, 5, 5, 5));
                GridPane.setHalignment(b, HPos.CENTER);
                GridPane.setValignment(b, VPos.CENTER);
            }
        }
        for (int i = 0; i < bCont.TheBoard.getChildren().size(); i++) {
            ((ButtonBase) bCont.TheBoard.getChildren().get(i)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    String flag = Objects.requireNonNull(getClass().getResource("images/flag.png")).toExternalForm();
                    String blank = Objects.requireNonNull(getClass().getResource("images/blank.png")).toExternalForm();
                    String mine = Objects.requireNonNull(getClass().getResource("images/mine.png")).toExternalForm();
                    String exposed = Objects.requireNonNull(getClass().getResource("images/exposed.png")).toExternalForm();
                    String hitmine = Objects.requireNonNull(getClass().getResource("images/hitmine.png")).toExternalForm();
                    String number1 = Objects.requireNonNull(getClass().getResource("images/number1.png")).toExternalForm();
                    String number2 = Objects.requireNonNull(getClass().getResource("images/number2.png")).toExternalForm();
                    String number3 = Objects.requireNonNull(getClass().getResource("images/number3.png")).toExternalForm();
                    String number4 = Objects.requireNonNull(getClass().getResource("images/number4.png")).toExternalForm();
                    String number5 = Objects.requireNonNull(getClass().getResource("images/number5.png")).toExternalForm();
                    String number6 = Objects.requireNonNull(getClass().getResource("images/number6.png")).toExternalForm();
                    String number7 = Objects.requireNonNull(getClass().getResource("images/number7.png")).toExternalForm();
                    String number8 = Objects.requireNonNull(getClass().getResource("images/number8.png")).toExternalForm();

                    int x, y;
                    y = (int) ((Button) event.getSource()).getProperties().get("gridpane-column");
                    x = (int) ((Button) event.getSource()).getProperties().get("gridpane-row");
                    if (event.getButton().equals(MouseButton.PRIMARY))
                        NewGameController.game.open(x, y);
                    else if (event.getButton().equals(MouseButton.SECONDARY))
                        NewGameController.game.toggleFlag(x, y);
//                    else if (event.getButton().equals(MouseButton.MIDDLE))
//                        NewGameController.game.toggleQM(x, y);
                    for (Node child : bCont.TheBoard.getChildren()) {
                        int j = (int) ((Button) child).getProperties().get("gridpane-column");
                        int i = (int) ((Button) child).getProperties().get("gridpane-row");
                        if (NewGameController.game.board[i][j].charAt(1) == 'F' && NewGameController.game.board[i][j].charAt(0) == 'D')
                            ((Button) child).setStyle("-fx-background-image: url('" + flag + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                        if (NewGameController.game.board[i][j].charAt(1) == 'F' && NewGameController.game.board[i][j].charAt(0) == 'N')
                            ((Button) child).setStyle("-fx-font-size:11");

                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == 'E') {
                            ((Button) child).setStyle("-fx-background-image: url('" + exposed + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setDisable(true);
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == 'M') {
                            ((Button) child).setStyle("-fx-background-image: url('" + mine + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == 'B') {
                            ((Button) child).setStyle("-fx-background-image: url('" + hitmine + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '1') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number1 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '2') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number2 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '3') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number3 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '4') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number4 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '5') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number5 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '6') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number6 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '7') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number7 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (NewGameController.game.board[i][j].charAt(1) == 'T' && NewGameController.game.board[i][j].charAt(2) == '8') {
                            ((Button) child).setStyle("-fx-background-image: url('" + number8 + "'); " +
                                    "-fx-background-position: center center; " +
                                    "-fx-background-repeat: stretch;");
                            ((Button) child).setText(" ");
                        }
                        if (Mines.winLose == 1) {
                            bCont.winLose.setVisible(true);
                            bCont.winLose.setText("YOU WIN!");
                        }
                        if (Mines.winLose == 0) {
                            bCont.winLose.setVisible(true);
                            bCont.winLose.setText("YOU LOSE! Try again...");
                        }
                        Mines.winLose = -1;
                    }
                }
            });
        }
        gameStage.show(); // Show stage }
    }

    @FXML
    void ApplicationCreate() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField scenario_id = new TextField();
        TextField level = new TextField();
        TextField mines = new TextField();
        TextField hypermine = new TextField();
        TextField time = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("SCENARIO-ID:"), 0, 0);
        grid.add(scenario_id, 1, 0);
        grid.add(new Label("Level of Difficulty"), 0, 1);
        grid.add(level, 1, 1);
        grid.add(new Label("Number of Mines"), 0, 2);
        grid.add(mines, 1, 2);
        grid.add(new Label("HyperMine"), 0, 3);
        grid.add(hypermine, 1, 3);
        grid.add(new Label("Time in Seconds"), 0, 4);
        grid.add(time, 1, 4);

        dialogPane.setContent(grid);

        dialog.setResultConverter((ButtonType button) -> {
            if (button == ButtonType.OK) {
                String[] list = {scenario_id.getText(), level.getText(), mines.getText(), hypermine.getText(), time.getText()};
                return list;
            }
            return null;
        });
        Optional<String[]> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((String[] list) -> {
//            System.out.println(
//                    list[0] + " " + list[1] + " " + list[2] + " " + list[3] + " " + list[4]);
            int[] numbers = Arrays.stream(list).mapToInt(Integer::parseInt).toArray();
            this.scenario_id = Integer.parseInt(list[0]);
            fc.createScenarioID(list[0], list[1], list[2], list[3], list[4]);
        });
    }

    @FXML
    void ApplicationLoad() {
    }

    @FXML
    void ApplicationStart() {
    }

    @FXML
    void DetailsRounds() {
    }

    @FXML
    void DetailsSolution() {
    }

}