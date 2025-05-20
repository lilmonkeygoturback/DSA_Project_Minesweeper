import java.awt.*;
import javax.swing.*;

public class MinesweeperFrame extends JFrame {
    private final int rows;
    private final int cols;
    private final int mines;
    private JButton[][] buttons;
    private boolean gameInProgress = false;
    private int currentRows, currentCols, currentMines;
    private JMenuItem continueItem;
    private int gridRows, gridCols, gridMines;
    private JPanel gridPanel;

    public MinesweeperFrame(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.currentRows = rows;
        this.currentCols = cols;
        this.currentMines = mines;
        this.gridRows = rows;
        this.gridCols = cols;
        this.gridMines = mines;
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setJMenuBar(createMenuBar());
        initUI();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem startItem = new JMenuItem("Start");
        continueItem = new JMenuItem("Continue");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenu difficultyMenu = new JMenu("Difficulty");
        JMenuItem easyItem = new JMenuItem("Easy (9x9, 10 mines)");
        JMenuItem mediumItem = new JMenuItem("Medium (16x16, 40 mines)");
        JMenuItem hardItem = new JMenuItem("Hard (16x30, 99 mines)");

        startItem.addActionListener(e -> {
            gameInProgress = true;
            continueItem.setEnabled(true);
            resetBoard(currentRows, currentCols, currentMines);
        });
        continueItem.addActionListener(e -> {
            if (gameInProgress) {
                this.requestFocus();
            }
        });
        continueItem.setEnabled(false);
        exitItem.addActionListener(e -> System.exit(0));

        easyItem.addActionListener(e -> changeDifficulty(9, 9, 10));
        mediumItem.addActionListener(e -> changeDifficulty(16, 16, 40));
        hardItem.addActionListener(e -> changeDifficulty(16, 30, 99));

        difficultyMenu.add(easyItem);
        difficultyMenu.add(mediumItem);
        difficultyMenu.add(hardItem);

        gameMenu.add(startItem);
        gameMenu.add(continueItem);
        gameMenu.addSeparator();
        gameMenu.add(difficultyMenu);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        menuBar.add(gameMenu);

        return menuBar;
    }

    private void resetBoard(int rows, int cols, int mines) {
        this.gridRows = rows;
        this.gridCols = cols;
        this.gridMines = mines;
        getContentPane().removeAll();
        initUI();
        revalidate();
        repaint();
    }

    private void changeDifficulty(int rows, int cols, int mines) {
        gameInProgress = false;
        continueItem.setEnabled(false);
        resetBoard(rows, cols, mines);
    }

    private void initUI() {
        if (gridPanel != null) {
            remove(gridPanel);
        }
        gridPanel = new JPanel(new GridLayout(gridRows, gridCols));
        buttons = new JButton[gridRows][gridCols];
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(40, 40));
                int row = r, col = c;
                btn.addActionListener(e -> handleCellClick(row, col));
                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
    }

    private void handleCellClick(int row, int col) {
        // TODO: Add game logic for revealing cells, handling mines, etc.
        buttons[row][col].setText("X"); // Placeholder for click
    }
}
