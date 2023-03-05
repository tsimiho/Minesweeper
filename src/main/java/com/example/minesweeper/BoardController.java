package com.example.minesweeper;

import java.io.IOException;
import java.util.*;
import java.lang.*;

import com.almasb.fxgl.app.GameController;
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

    static Mines game;
    public int numberOfFlags, level, mines, hypermine, time, rows = 9, columns = 9;
    public FileController fc = new FileController();
    //    public NewGameController gc;
    public String scenario_id;
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
    Stage gameStage = new Stage();
    Button b;
    BoardController bCont;

    public BoardController() {
    }

    public BoardController(int[] input) throws IOException {
        level = input[0];
        mines = input[1];
        hypermine = input[2];
        time = input[3];
        numberOfFlags = mines;

        loader = new FXMLLoader(getClass().getResource("BoardFXML.fxml"));

        AnchorPane root = loader.load(); // Load layout
        Scene scene = new Scene(root); // Create scene with chosen layout

        SetDimensions();

        gameStage.setTitle("MediaLab Minesweeper"); // Set stage's title
        bCont = loader.getController();
        bCont.SetMineView(mines);
        bCont.SetFlagCount(numberOfFlags);
        gameStage.setScene(scene);

        StartGame();

    }

    void SetMineView(int m) {
        MineView.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/mine.png")).toExternalForm()));
//        MineCount = new Label();
        MineCount.setText(Integer.toString(m));
    }

    void SetFlagCount(int f) {
        FlagView.setImage(new Image(Objects.requireNonNull(getClass().getResource("images/flag.png")).toExternalForm()));
//        MineCount = new Label();
        FlagCount.setText(Integer.toString(f));
    }

    void SetDimensions() {
        if (level == 1) {
            gameStage.setHeight(600);
            gameStage.setWidth(600);
        } else if (level == 2) {
            gameStage.setHeight(800);
            gameStage.setWidth(800);
        }
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
                String[] list = {scenario_id.getText(), level.getText(), mines.getText(), time.getText(), hypermine.getText()};
                return list;
            }
            return null;
        });
        Optional<String[]> optionalResult = dialog.showAndWait();
        optionalResult.ifPresent((String[] list) -> {
            this.scenario_id = list[0];
            fc.createScenarioID(list[0], list[1], list[2], list[3], list[4]);
        });
    }

    @FXML
    void ApplicationLoad() throws InvalidDescriptionException, InvalidValueException {
        int[] data = fc.getGameDescription(scenario_id);
        level = data[0];
        mines = data[1];
        time = data[2];
        hypermine = data[3];
    }

    @FXML
    void ApplicationStart(ActionEvent event) throws IOException, InvalidDescriptionException, InvalidValueException {
        loader = new FXMLLoader(getClass().getResource("BoardFXML.fxml"));
//
        AnchorPane root = loader.load(); // Load layout
        Scene scene = new Scene(root); // Create scene with chosen layout

        gameStage = (Stage) MyMenuBar.getScene().getWindow();
        SetDimensions();
        bCont = loader.getController();

        bCont.SetMineView(mines);

        gameStage.setScene(scene);

        bCont = loader.getController();

        bCont.scenario_id = scenario_id;
        bCont.ApplicationLoad();
        StartGame();
    }

    @FXML
    void DetailsRounds() {
    }

    @FXML
    void DetailsSolution() {
    }

    String GetImage(String ImageUrl) {
        return Objects.requireNonNull(getClass().getResource(ImageUrl)).toExternalForm();
    }

    @FXML
    void StartGame() throws IOException {
        if (level == 1) {
            rows = 9;
            columns = 9;
        } else if (level == 2) {
            rows = 16;
            columns = 16;
        }

        game = new Mines(rows, columns, mines);

//        FXMLLoader loader = new FXMLLoader(); // Create loading object
//        loader.setLocation(getClass().getResource("BoardFXML.fxml")); // fxml location
//
//        AnchorPane root = loader.load(); // Load layout
//        Scene scene = new Scene(root); // Create scene with chosen layout
////        Stage gameStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
////        Stage gameStage = new Stage();
//
//        gameStage.setTitle("MediaLab Minesweeper"); // Set stage's title
//
//        gameStage.setScene(scene); // Set scene to stage
//
//        BoardController bCont = loader.getController(); // Prepare board in BoardCONTROLLER

        bCont.winLose.setVisible(false);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
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
                    int x, y;
                    y = (int) ((Button) event.getSource()).getProperties().get("gridpane-column");
                    x = (int) ((Button) event.getSource()).getProperties().get("gridpane-row");
                    if (event.getButton().equals(MouseButton.PRIMARY))
                        game.open(x, y);
                    else if (event.getButton().equals(MouseButton.SECONDARY))
                        game.toggleFlag(x, y);
                    for (Node child : bCont.TheBoard.getChildren()) {
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
                        if (game.board[i][j].charAt(1) == 'T' && game.board[i][j].charAt(2) == 'M') {
                            ((Button) child).setStyle("-fx-background-image: url('" + GetImage("images/mine.png") + "'); " +
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
                        if (game.winLose == 1) {
                            bCont.winLose.setVisible(true);
                            bCont.winLose.setText("YOU WIN!");
                        }
                        if (game.winLose == 0) {
                            bCont.winLose.setVisible(true);
                            bCont.winLose.setText("YOU LOSE! Try again...");
                        }
                        Mines.winLose = -1;
                        numberOfFlags = mines - game.getNumberOfFlags();
                        bCont.SetFlagCount(numberOfFlags);
                    }
                }
            });
        }
        gameStage.show(); // Show stage
    }


}