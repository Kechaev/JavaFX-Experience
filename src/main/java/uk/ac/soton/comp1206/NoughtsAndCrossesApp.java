package uk.ac.soton.comp1206;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class NoughtsAndCrossesApp extends Application {
    private int size = 3;
    private GameBoard gameBoard;
    private boolean isGameOver;
    private int NoWinsX = 0;
    private int NoWinsO = 0;
    private String player1Name;
    private String player2Name;

    private Button[][] buttons;
    private Label scoreLabel;
    private Label player1Label;
    private Label player2Label;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        Scene scene = showLoadingScreen();
        stage.setTitle("Menu");
        stage.setScene(scene);
        stage.show();
    }

    private Scene showLoadingScreen() {
        Label title = new Label("Noughts & Crosses");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label p1Label = new Label("Player 1 (X) name:");
        TextField p1Field = new TextField();
        p1Field.setPromptText("Enter name...");

        Label p2Label = new Label("Player 2 (O) name:");
        TextField p2Field = new TextField();
        p2Field.setPromptText("Enter name...");

        Label sizeLabel = new Label("Board size:");
        Spinner<Integer> sizeSpinner = new Spinner<>(3, 9, 3);
        sizeSpinner.setEditable(true);

        Button startButton = new Button("Start Game");

        startButton.disableProperty().bind(p1Field.textProperty().isEmpty().or(p2Field.textProperty().isEmpty())
        );

        startButton.setOnAction(e -> {
            player1Name = p1Field.getText().trim();
            player2Name = p2Field.getText().trim();
            size = sizeSpinner.getValue();
            stage.setScene(showGameScreen());
            gameBoard = new GameBoard(size);
            isGameOver = false;
        });

        VBox layout = new VBox(10,
                title,
                p1Label, p1Field,
                p2Label, p2Field,
                sizeLabel, sizeSpinner,
                startButton
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        return new Scene(layout, 320, 320);
    }

    private Scene showGameScreen() {
        Label title = new Label("Noughts & Crosses");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        buttons = new Button[size][size];
        GridPane grid = new GridPane();

        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(new NoughtsAndCrossesApp.ResetButtonClickHandler());
        scoreLabel = new Label("X Wins: 0 | O Wins: 0");

        Button scoreboardButton = new Button("Scoreboard");
        scoreboardButton.setOnAction(e -> showScoreboard());

        player1Label = new Label(player1Name);
        player1Label.setStyle("-fx-font-weight: bold;");
        Label separatorLabel = new Label(" | ");
        player2Label = new Label(player2Name);
        root.getChildren().addAll(resetButton, scoreLabel);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button newButton = new Button("");
                newButton.setPrefSize(100,100);
                newButton.setStyle("-fx-font-size: 24px;");
                newButton.setOnAction(new NoughtsAndCrossesApp.ButtonClickHandler(i,j));
                buttons[i][j] = newButton;
                grid.add(buttons[i][j], i, j);
            }
        }

        root.getChildren().addAll(grid);

        HBox playerLabels = new HBox(10, player1Label, separatorLabel, player2Label);
        playerLabels.setAlignment(Pos.CENTER);
        VBox layout = new VBox(10,
                title,
                resetButton,
                scoreboardButton,
                scoreLabel,
                playerLabels,
                grid
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);

        return new Scene(layout, size * 100, 100 + size * 100);
    }

    private void updateTurnLabels() {
        if (gameBoard.getCurrentPlayer().equals("X")) {
            player1Label.setStyle("-fx-font-weight: bold;");
            player2Label.setStyle("-fx-font-weight: normal;");
        } else {
            player1Label.setStyle("-fx-font-weight: normal;");
            player2Label.setStyle("-fx-font-weight: bold;");
        }
    }

    private class ResetButtonClickHandler implements EventHandler<ActionEvent> {
        public ResetButtonClickHandler() {}

        public void handle(ActionEvent actionEvent) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    buttons[i][j].setText("");
                }
            }

            gameBoard.resetBoard();
            isGameOver = false;
        }
    }

    private class ButtonClickHandler implements EventHandler<ActionEvent> {
        private int column;
        private int row;

        public ButtonClickHandler(int row, int column) {
            this.column = column;
            this.row = row;
        }

        public void handle(ActionEvent event) {
            if (gameBoard.makeMove(row, column) && !isGameOver) {
                buttons[row][column].setText(gameBoard.getCurrentPlayer());
                gameBoard.switchPlayer();
                updateTurnLabels();

                if (!gameBoard.checkWinner().isEmpty()) {
                    isGameOver = true;

                    showWinnerDialog();

                    if (gameBoard.getCurrentPlayer().equals("X")) {
                        NoWinsX++;
                    } else {
                        NoWinsO++;
                    }
                    scoreLabel.setText("X Wins: " + NoWinsX + " | O Wins: " + NoWinsO);
                }
            }
        }
    }

    private void showWinnerDialog() {
        gameBoard.switchPlayer();
        String winnerName;
        if (gameBoard.getCurrentPlayer().equals("X")) {
            winnerName = player1Name;
        }
        else {
            winnerName = player2Name;
        }
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Game Over");
        alert.setHeaderText(winnerName + " wins!");
        alert.getButtonTypes().add(ButtonType.OK);
        alert.showAndWait();
    }

    private void showScoreboard() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Scoreboard");
        alert.setHeaderText(player1Name + "'s wins: " + NoWinsX + "\n" + player2Name + "'s wins: " + NoWinsO);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
