package uk.ac.soton.comp1206;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

public class NoughtsAndCrosses extends Application {
    private int width = 3;
    private int height = 3;
    private Button[][] buttons;
    private boolean isFirstPersonsTurn;
    private Label statusLabel;
    private Label scoreLabel;
    private int NoWinsX;
    private int NoWinsY;
    private boolean gameOver;
    private int[][] matrix;

    public void start(Stage stage) {
        Dialog<Pair<Integer, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Set Size");
        dialog.setHeaderText("Enter Grid Dimensions:");
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);
        GridPane dialogGrid = new GridPane();
        dialogGrid.setHgap(10);
        dialogGrid.setVgap(10);
        TextField xField = new TextField();
        TextField yField = new TextField();

        dialogGrid.add(new Label("Rows:"),0,0);
        dialogGrid.add(xField,1,0);
        dialogGrid.add(new Label("Columns:"),0,1);
        dialogGrid.add(yField,1,1);

        dialog.getDialogPane().setContent(dialogGrid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                try {
                    int x = Integer.parseInt(xField.getText());
                    int y = Integer.parseInt(yField.getText());
                    return new Pair<>(x,y);
                }
                catch (NumberFormatException e) {
                    return new Pair<>(3,3);
                }
            }
            return new Pair<>(3,3);
        });

        var result = dialog.showAndWait();

        if (result.isPresent()) {
            width = result.get().getKey();
            height = result.get().getValue();
        }

        buttons = new Button[width][height];
        matrix = new int[width][height];
        GridPane grid = new GridPane();

        VBox root = new VBox();
        root.setSpacing(10);
        root.setAlignment(Pos.CENTER);

        Button resetButton = new Button("Reset");
        resetButton.setOnAction(new ResetButtonClickHandler());
        scoreLabel = new Label("X Wins: 0 | O Wins: 0");
        statusLabel = new Label("");


        root.getChildren().addAll(resetButton, scoreLabel);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Button newButton = new Button("");
                newButton.setPrefSize(100,100);
                newButton.setStyle("-fx-font-size: 24px;");
                newButton.setOnAction(new ButtonClickHandler(i,j));
                buttons[i][j] = newButton;
                grid.add(buttons[i][j], i, j);
            }
        }

        root.getChildren().addAll(grid, statusLabel);

        Scene scene = new Scene(root, width * 100, 100 + height * 100);
        stage.setTitle("Noughts & Crosses");
        stage.setScene(scene);
        stage.show();

        gameOver = false;
        isFirstPersonsTurn = true;
        NoWinsX = 0;
        NoWinsY = 0;
    }

    private void takeTurn() {
        isFirstPersonsTurn = !isFirstPersonsTurn;
    }

    private class ResetButtonClickHandler implements EventHandler<ActionEvent> {
        public ResetButtonClickHandler() {}

        public void handle(ActionEvent actionEvent) {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    buttons[i][j].setText("");
                }
            }

            statusLabel.setText("");
            scoreLabel.setText("X Wins: " + NoWinsX + " | O Wins: " + NoWinsY);
            gameOver = false;
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
            if (!gameOver) {
                if (isFirstPersonsTurn) {
                    buttons[row][column].setText("X");
                } else {
                    buttons[row][column].setText("O");
                }
                takeTurn();

                char winner = whoHasWon();
                System.out.println(winner);

                if (winner != '\0') {
                    statusLabel.setText(winner + " won!");

                    if (winner == 'X') {
                        NoWinsX++;
                    } else {
                        NoWinsY++;
                    }
                    gameOver = true;
                }
            }
        }
    }

    private char whoHasWon() {
        int[][] matrix = getMatrix(buttons);

        for (int i = 0; i < width; i++) {
            int sum = 0;
            for (int j = 0; j < height; j++) {
                sum += matrix[i][j];
            }
            if (sum == height) return 'X';
            if (sum == -height) return 'O';
        }

        for (int i = 0; i < height; i++) {
            int sum = 0;
            for (int j = 0; j < width; j++) {
                sum += matrix[j][i];
            }
            if (sum == width) return 'X';
            if (sum == -width) return 'O';
        }

        int diag = 0;
        int antiDiag = 0;
        for (int i = 0; i < min(width, height); i++) {
            diag += matrix[i][i];
            antiDiag += matrix[i][min(width, height) - 1 - i];
        }
        if (diag == min(width, height) || antiDiag == min(width, height)) return 'X';
        if (diag == -min(width, height) || antiDiag == -min(width, height)) return 'O';

        return '\0';
    }

    private int min(int a, int b) {
        return Math.min(a,b);
    }

    private int[][] getMatrix(Button[][] buttonMap) {
        int[][] matrix = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (buttonMap[i][j].getText().equals("X")) {
                    matrix[i][j] = 1;
                }
                else if (buttonMap[i][j].getText().equals("O")) {
                    matrix[i][j] = -1;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }

    public static void main(String[] args) {
        launch();
    }
}
