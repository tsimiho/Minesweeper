package com.example.minesweeper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.lang.*;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Popup;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;


import java.util.Optional;

public class BoardController implements Initializable {
    public Mines game;
    public int numberOfFlags, level, mines, hypermine, time, rows = 9, columns = 9, countdownDuration, clicks;
    public FileController fc = new FileController();
    public String scenario_id;
    public Stage stage;
    @FXML
    public GridPane TheBoard;
    @FXML
    public Label winLose;
    public FXMLLoader loader;
    @FXML
    public ImageView MineView;
    @FXML
    public Label MineCount;
    @FXML
    public ImageView FlagView;
    @FXML
    public Label FlagCount;
    @FXML
    MenuBar MyMenuBar;
    Button b;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty();
    private Timeline timeline;
    @FXML
    private Label Time = new Label();

    public BoardController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        level = 1;
        mines = 10;
        time = 150;
        numberOfFlags = 0;

        InitTimer();
        SetMineView();
        SetFlagCount();
        StartGame();
    }

    void SetMineView() {
        MineView.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/mine.png")).toExternalForm()));
        MineCount.setText(Integer.toString(mines));
    }

    void SetFlagCount() {
        FlagView.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/flag.png")).toExternalForm()));
        FlagCount.setText(Integer.toString(numberOfFlags));
    }

    void InitTimer() {
        clicks = 0;
        countdownDuration = time;
        timeSeconds.set(countdownDuration);
        Time.textProperty().bind(this.timeSeconds.asString());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeSeconds.set(timeSeconds.get() - 1);
                if (timeSeconds.get() == 0) {
                    timeline.stop();
                    Mines.setShowAll();
                    SetTheBoard();
                    Mines.recordResult(0, time - timeSeconds.get());
                    winLose.setVisible(true);
                    winLose.setText("Time is up!");
                }
            }
        }));
    }

    void StartGame() {
        if (level == 1) {
            rows = 9;
            columns = 9;
        } else if (level == 2) {
            rows = 16;
            columns = 16;
        }

        game = new Mines(rows, columns, mines, hypermine);

        winLose.setVisible(false);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                b = new Button(" ");
                b.setMinSize(30, 30);
                b.setMaxSize(30, 30);
                b.setStyle("-fx-font-size:11");
                TheBoard.add(b, i, j);
                TheBoard.setMargin(b, new Insets(5, 5, 5, 5));
                TheBoard.setHalignment(b, HPos.CENTER);
                TheBoard.setValignment(b, VPos.CENTER);
            }
        }
        for (int i = 0; i < TheBoard.getChildren().size(); i++) {
            ((ButtonBase) TheBoard.getChildren().get(i)).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if (clicks == 0) {
                        timeline.playFromStart();
                        clicks++;
                    }
                    int x, y;
                    y = (int) ((Button) event.getSource()).getProperties().get("gridpane-column");
                    x = (int) ((Button) event.getSource()).getProperties().get("gridpane-row");
                    if (event.getButton().equals(MouseButton.PRIMARY))
                        game.open(x, y, true);
                    else if (event.getButton().equals(MouseButton.SECONDARY))
                        game.toggleFlag(x, y);
                    SetTheBoard();
                }
            });
        }
    }

    void SetTheBoard() {
        for (Node child : TheBoard.getChildren()) {
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
                timeline.stop();
                game.setShowAll();
                Mines.recordResult(1, time - timeSeconds.get());
                winLose.setVisible(true);
                winLose.setText("You won!");
            }
            if (Mines.winLose == 0) {
                timeline.stop();
                game.setShowAll();
                Mines.recordResult(0, time - timeSeconds.get());
                winLose.setVisible(true);
                winLose.setText("You hit a mine!");
            }
            Mines.winLose = -1;
            numberOfFlags = game.getNumberOfFlags();
            SetFlagCount();

        }
    }

    @FXML
    void ApplicationCreate() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Create Game");
        DialogPane dialogPane = dialog.getDialogPane();
        ButtonType okButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(okButtonType, cancelButtonType);
        TextField scenario_id = new TextField();
        TextField level = new TextField();
        TextField mines = new TextField();
        TextField hypermine = new TextField();
        TextField time = new TextField();
        Label errorLabel = new Label("Invalid Input");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

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
        grid.add(new Label("HyperMine (Yes/No)"), 0, 3);
        grid.add(hypermine, 1, 3);
        grid.add(new Label("Time in Seconds"), 0, 4);
        grid.add(time, 1, 4);
        grid.add(errorLabel, 1, 5);
        dialogPane.setContent(grid);

        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            System.out.println(Objects.equals(hypermine.getText(), "No"));
            if (Objects.equals(hypermine.getText(), "Yes") || Objects.equals(hypermine.getText(), "No")) {
                String h = hypermine.getText() == "Yes" ? "1" : "0";
                String[] list = {scenario_id.getText(), level.getText(), mines.getText(), time.getText(), h};
                this.scenario_id = list[0];
                fc.createScenarioID(list[0], list[1], list[2], list[3], list[4]);
            } else {
                errorLabel.setVisible(true);
                event.consume();
            }
        });

        dialog.showAndWait();
    }

    @FXML
    void ApplicationLoad() throws InvalidDescriptionException, InvalidValueException {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Load Game");
        DialogPane dialogPane = dialog.getDialogPane();

        ButtonType okButtonType = new ButtonType("Load", ButtonBar.ButtonData.APPLY);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(okButtonType, cancelButtonType);
        TextField id = new TextField();

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("SCENARIO-ID:"), 0, 0);
        grid.add(id, 1, 0);
        GridPane.setRowSpan(errorLabel, 1);
        GridPane.setColumnSpan(errorLabel, 2);
        grid.add(errorLabel, 0, 1);
        Button okButton = (Button) dialog.getDialogPane().lookupButton(okButtonType);

        dialogPane.setContent(grid);

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            String s_id = id.getText();
            if (!fc.ScenarioExists(s_id)) {
                errorLabel.setVisible(true);
                errorLabel.setText("The specified section-id does not exist.");
                event.consume();
            } else {
                scenario_id = s_id;
                int[] data = new int[0];
                try {
                    data = fc.getGameDescription(scenario_id);
                } catch (InvalidDescriptionException | InvalidValueException e) {
                    errorLabel.setVisible(true);
                    errorLabel.setText("The file contents are invalid");
                    throw new RuntimeException(e);
                }
                level = data[0];
                mines = data[1];
                time = data[2];
                hypermine = data[3];
            }
        });

        dialog.showAndWait();


    }

    @FXML
    void ApplicationStart() {
        TheBoard.getChildren().clear();

        stage = (Stage) TheBoard.getScene().getWindow();
        if (level == 1) {
            stage.setHeight(650);
            stage.setWidth(650);
        } else if (level == 2) {
            stage.setHeight(900);
            stage.setWidth(900);
        }

        timeline.stop();
        InitTimer();
        SetMineView();
        SetFlagCount();
        StartGame();
    }

    @FXML
    void DetailsRounds() {
        int[][] result = fc.GetResults();
        int counter = 0;
        for (int i = 0; i < 5; i++) {
            if (result[i][0] != 0) {
                counter++;
            }
        }
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        Label[] rowLabels = new Label[counter];
        Label[] colLabels = new Label[4];
        for (int i = 0; i < counter; i++) {
            rowLabels[i] = new Label((i + 1) + ".");
            gridPane.add(rowLabels[i], 0, i + 1);
        }
        colLabels[0] = new Label("Mines");
        colLabels[0].setAlignment(Pos.CENTER);
        gridPane.add(colLabels[0], 1, 0);
        colLabels[1] = new Label("Tries");
        colLabels[1].setAlignment(Pos.CENTER);
        gridPane.add(colLabels[1], 2, 0);
        colLabels[2] = new Label("Game Time");
        colLabels[2].setAlignment(Pos.CENTER);
        gridPane.add(colLabels[2], 3, 0);
        colLabels[3] = new Label("Winner");
        colLabels[3].setAlignment(Pos.CENTER);
        gridPane.add(colLabels[3], 4, 0);

        Label[][] matrixLabels = new Label[counter][4];
        for (int i = 0; i < counter; i++) {
            for (int j = 0; j < 4; j++) {
                String s;
                if (j == 3) {
                    s = result[i][j] == 0 ? "Computer" : "User";
                } else if (j == 2) {
                    s = String.valueOf(result[i][j]) + " seconds";
                } else {
                    s = String.valueOf(result[i][j]);
                }
                matrixLabels[i][j] = new Label(s);
                gridPane.add(matrixLabels[i][j], j + 1, i + 1);
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rounds");
        alert.setHeaderText(null);
        alert.setGraphic(null);

        alert.getDialogPane().setContent(gridPane);

        ButtonType buttonTypeOne = new ButtonType("OK");
        alert.getButtonTypes().setAll(buttonTypeOne);

        alert.showAndWait();
    }

    @FXML
    void DetailsSolution() {
        timeline.stop();
        Mines.setShowAll();
        SetTheBoard();
        Mines.recordResult(0, time - timeSeconds.get());
        winLose.setVisible(true);
        winLose.setText("You hit a mine!");
    }

    String GetImage(String ImageUrl) {
        return Objects.requireNonNull(getClass().getResource(ImageUrl)).toExternalForm();
    }
}