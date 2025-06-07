import java.util.*;

public class BoardLogic {
    public Cell[][] grid;
    private int rows, cols, mines;
    private boolean[][] visited;

    public BoardLogic(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        grid = new Cell[rows][cols];
        visited = new boolean[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = new Cell();

        placeMines();
        calculateAdjacents();
    }

    private void placeMines() {
        Random rand = new Random();
        int placed = 0;
        while (placed < mines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (!grid[r][c].isMine) {
                grid[r][c].isMine = true;
                placed++;
            }
        }
    }

    private void calculateAdjacents() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (!grid[r][c].isMine)
                    grid[r][c].adjacentMines = countAdjacentMines(r, c);
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++)
            for (int dc = -1; dc <= 1; dc++) {
                int nr = row + dr, nc = col + dc;
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc].isMine)
                    count++;
            }
        return count;
    }

    // using BFS to reveal cells
    public void reveal(int row, int col) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{row, col});
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int r = curr[0], c = curr[1];
            if (r < 0 || r >= rows || c < 0 || c >= cols || visited[r][c])
                continue;
            visited[r][c] = true;
            Cell cell = grid[r][c];
            cell.isRevealed = true;

            if (cell.adjacentMines == 0 && !cell.isMine) {
                for (int dr = -1; dr <= 1; dr++)
                    for (int dc = -1; dc <= 1; dc++)
                        queue.add(new int[]{r + dr, c + dc});
            }
        }
    }

    public boolean isGameWon() {
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                Cell cell = grid[r][c];
                if (!cell.isMine && !cell.isRevealed)
                    return false;
            }
        return true;
    }
}

