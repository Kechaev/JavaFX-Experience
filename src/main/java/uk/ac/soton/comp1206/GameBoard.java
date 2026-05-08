package uk.ac.soton.comp1206;

import javafx.scene.control.Button;

public class GameBoard {
    private String[][] board;
    private int size;
    private String currentPlayer;
    private int scoreToWin;

    public GameBoard(int size) {
        this.size = size;
        if (size > 4) {
            scoreToWin = size - 2;
        }
        else {
            scoreToWin = size;
        }
        board = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "";
            }
        }

        currentPlayer = "X";
    }

    public void resetBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = "";
            }
        }
    }

    public void switchPlayer() {
        if (currentPlayer == "X") {
            currentPlayer = "O";
        }
        else {
            currentPlayer = "X";
        }
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col].isEmpty()) {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
    }

    public String checkWinner() {
        int[][] matrix = getMatrix();

        for (int i = 0; i < size; i++) {
            int sum = 0;
            for (int j = 0; j < size; j++) {
                sum += matrix[i][j];
            }
            if (sum == scoreToWin) return "X";
            if (sum == -scoreToWin) return "O";
        }

        for (int i = 0; i <= size - scoreToWin; i++) {
            for (int j = 0; j <= size - scoreToWin; j++) {
                int diag = 0;
                for (int k = 0; k < scoreToWin; k++) {
                    diag += matrix[i + k][j + k];
                }
                if (diag == scoreToWin) return "X";
                if (diag == -scoreToWin) return "O";
            }
        }

        for (int i = 0; i <= size - scoreToWin; i++) {
            for (int j = scoreToWin - 1; j < size; j++) {
                int antiDiag = 0;
                for (int k = 0; k < scoreToWin; k++) {
                    antiDiag += matrix[i + k][j - k];
                }
                if (antiDiag == scoreToWin) return "X";
                if (antiDiag == -scoreToWin) return "O";
            }
        }

        return "";
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    private int[][] getMatrix() {
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].equals("X")) {
                    matrix[i][j] = 1;
                }
                else if (board[i][j].equals("O")) {
                    matrix[i][j] = -1;
                }
                else {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }
}
