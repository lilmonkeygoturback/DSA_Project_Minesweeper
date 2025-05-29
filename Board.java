import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
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
    private static class Point {
    int r, c;
    Point(int r, int c) {
        this.r = r;
        this.c = c;
        }
    }

    private void initBoard() {
        // Place mines randomly and fill numbers
        // ...existing code...
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (board[r][c] != '*') {
                board[r][c] = '*';
                placed++;
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != '*') {
                    int count = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int nr = r + dr, nc = c + dc;
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && board[nr][nc] == '*') {
                                count++;
                            }
                        }
                    }
                    board[r][c] = count == 0 ? ' ' : (char) (count + '0');
                }
            }
        }
    }

    public void play(Scanner scanner) {
        // Main game loop: print board, get user input, reveal/flag cells, check win/loss
        // ...existing code...
        while (!gameOver) {
            printBoardWithFrame();
            System.out.print("Enter command (r row col | f row col): ");
            String action = scanner.next();
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row < 0 || row >= rows || col < 0 || col >= cols) {
                System.out.println("Invalid coordinates.");
                continue;
            }

            if (action.equalsIgnoreCase("r")) {
                revealCell(row, col);
            } else if (action.equalsIgnoreCase("f")) {
                flagCell(row, col);
            }

            if (isWin()) {
                printBoardWithFrame();
                System.out.println("Congratulations! You win!");
                break;
            }

            if (gameOver) {
                printBoardWithFrame();
                System.out.println("Game over! You hit a mine.");
            }
        }
    }

    // Add methods for revealing cells, flagging, checking win/loss, BFS/DFS for empty cells
    // ...existing code...
    private void revealCell (int startR, int startC) {
    if (revealed[startR][startC] || flagged[startR][startC]) return;

    Queue<Point> queue = new LinkedList<>();
    queue.add(new Point(startR, startC));

    while (!queue.isEmpty()) {
        Point p = queue.poll();
        int r = p.r, c = p.c;

        if (r < 0 || r >= rows || c < 0 || c >= cols || revealed[r][c] || flagged[r][c])
            continue;

        revealed[r][c] = true;

        if (board[r][c] == '*') {
            gameOver = true;
            return;
        }

        if (board[r][c] == ' ') {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr != 0 || dc != 0) {
                        queue.add(new Point(r + dr, c + dc));
                       }
                    }
                }
            }
        }
    }
    private void flagCell(int r, int c) {
        if (!revealed[r][c]) {
            flagged[r][c] = !flagged[r][c];
        }
    }

    private boolean isWin() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != '*' && !revealed[r][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printBoardWithFrame() {
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        System.out.print("  +");
        for (int c = 0; c < cols; c++) {
            System.out.print("--");
        }
        System.out.println("+");
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
        System.out.print("  +");
        for (int c = 0; c < cols; c++) {
            System.out.print("--");
        }
        System.out.println("+");
    }
}