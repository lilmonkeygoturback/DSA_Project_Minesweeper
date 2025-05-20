import java.util.Scanner;

public class Board {
    private final int rows;
    private final int cols;
    private final int mines;
    private final char[][] board;
    private final boolean[][] revealed;
    private final boolean[][] flagged;
    private boolean gameOver;

    public Board(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.board = new char[rows][cols];
        this.revealed = new boolean[rows][cols];
        this.flagged = new boolean[rows][cols];
        this.gameOver = false;
        initBoard();
    }

    private void initBoard() {
        // Place mines randomly and fill numbers
        // ...existing code...
    }

    public void play(Scanner scanner) {
        // Main game loop: print board, get user input, reveal/flag cells, check win/loss
        // ...existing code...
    }

    // Add methods for revealing cells, flagging, checking win/loss, BFS/DFS for empty cells
    // ...existing code...

    // Print the board with a visible frame (borders and coordinates)
    private void printBoardWithFrame() {
        // Print column headers
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        // Print top border
        System.out.print("  +");
        for (int c = 0; c < cols; c++) {
            System.out.print("--");
        }
        System.out.println("+");
        // Print each row
        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d|", r);
            for (int c = 0; c < cols; c++) {
                if (flagged[r][c]) {
                    System.out.print("F ");
                } else if (!revealed[r][c]) {
                    System.out.print("# ");
                } else {
                    System.out.print(board[r][c] + " ");
                }
            }
            System.out.println("|");
        }
        // Print bottom border
        System.out.print("  +");
        for (int c = 0; c < cols; c++) {
            System.out.print("--");
        }
        System.out.println("+");
    }
}