import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
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
    private BoardLogic boardLogic;
    private ImageIcon[] numberIcons = new ImageIcon[9];
    private ImageIcon mineIcon, flagIcon, unknownIcon, explodedIcon;
    private JLabel mineCounterLabel;
    private JLabel timerLabel;
    private Timer gameTimer;
    private int secondsElapsed;
    private JButton smileyButton;
    private static final int TILE_SIZE = 40; // Match button size


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

    private void startTimer() {
        if (gameTimer != null) gameTimer.stop();
        secondsElapsed = 0;
        timerLabel.setText("Time: 0");
        gameTimer = new Timer(1000, e -> {
            secondsElapsed++;
            timerLabel.setText("Time: " + secondsElapsed);
        });
        gameTimer.start();
    }

    private void stopTimer() {
        if (gameTimer != null) gameTimer.stop();
    }

    private void updateMineCounter() {
        int flags = 0;
        for (int r = 0; r < gridRows; r++)
            for (int c = 0; c < gridCols; c++)
                if (boardLogic.grid[r][c].isFlagged) flags++;
        mineCounterLabel.setText("Mines: " + (gridMines - flags));
    }

    private void resetBoard(int rows, int cols, int mines) {
        this.gridRows = rows;
        this.gridCols = cols;
        this.gridMines = mines;
        if (gameTimer != null) gameTimer.stop();
        getContentPane().removeAll();
        setSize((TILE_SIZE + 2) * gridCols, (TILE_SIZE + 2) * gridRows + 100); // Dynamically resize frame
        gameInProgress = true; // Ensure game is active after reset
        initUI();
        revalidate();
        repaint();
        pack(); // Ensure frame fits new grid
    }

    private void changeDifficulty(int rows, int cols, int mines) {
        gameInProgress = false;
        continueItem.setEnabled(false);
        resetBoard(rows, cols, mines);
    }

    private ImageIcon loadAndScaleIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(TILE_SIZE, TILE_SIZE, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void loadIcons() {
        for (int i = 1; i <= 8; i++) {
            numberIcons[i] = loadAndScaleIcon("assets/Sprites/Tile" + i + ".png");
        }
        numberIcons[0] = loadAndScaleIcon("assets/Sprites/TileEmpty.png");
        mineIcon = loadAndScaleIcon("assets/Sprites/TileMine.png");
        flagIcon = loadAndScaleIcon("assets/Sprites/TileFlag.png");
        unknownIcon = loadAndScaleIcon("assets/Sprites/TileUnknown.png");
        explodedIcon = loadAndScaleIcon("assets/Sprites/TileExploded.png");
    }

    // --- IMPROVEMENTS ---
    // 1. Always initialize boardLogic in initUI/resetBoard
    // 2. Add right-click flagging
    // 3. Use exploded icon for the mine that ends the game
    // 4. Always update buttons after board changes

    private int explodedRow = -1, explodedCol = -1;

    private void initUI() {
        loadIcons();
        if (gridPanel != null) {
            remove(gridPanel);
        }
        // --- Top Panel Layout ---
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(2, 2, 2, 2);

        mineCounterLabel = new JLabel("Mines: " + gridMines, SwingConstants.LEFT);
        mineCounterLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        timerLabel = new JLabel("Time: 0", SwingConstants.RIGHT);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 16));
        smileyButton = new JButton(":)");
        smileyButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        smileyButton.setMargin(new Insets(0,0,0,0));
        smileyButton.addActionListener(e -> startNewGame());
        smileyButton.setFocusable(false);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.5;
        topPanel.add(mineCounterLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 0;
        topPanel.add(smileyButton, gbc);
        gbc.gridx = 2; gbc.weightx = 0.5;
        topPanel.add(timerLabel, gbc);
        add(topPanel, BorderLayout.NORTH);

        // --- Game Grid ---
        gridPanel = new JPanel(new GridLayout(gridRows, gridCols, 1, 1));
        gridPanel.setBackground(new Color(180, 200, 230));
        buttons = new JButton[gridRows][gridCols];
        boardLogic = new BoardLogic(gridRows, gridCols, gridMines);
        explodedRow = -1;
        explodedCol = -1;
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                btn.setMinimumSize(new Dimension(TILE_SIZE, TILE_SIZE));
                btn.setMaximumSize(new Dimension(TILE_SIZE, TILE_SIZE));
                btn.setIcon(unknownIcon);
                btn.setBackground(Color.WHITE);
                btn.setBorder(BorderFactory.createBevelBorder(1));
                int row = r, col = c;
                btn.setMargin(new Insets(0,0,0,0));
                btn.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        if (!gameInProgress) return;
                        if (SwingUtilities.isRightMouseButton(e)) {
                            handleFlagCell(row, col);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            handleCellClick(row, col);
                        }
                    }
                });
                buttons[r][c] = btn;
                gridPanel.add(btn);
            }
        }
        add(gridPanel, BorderLayout.CENTER);
        updateButtons();
        startTimer();
    }

    private void playBombSound() {
        try {
            File soundFile = new File("assets/sound/break-boom-fx-240235.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Could not play bomb sound: " + e.getMessage());
        }
    }

    private void handleCellClick(int row, int col) {
        if (row < 0 || row >= gridRows || col < 0 || col >= gridCols) return;
        Cell cell = boardLogic.grid[row][col];
        if (cell.isRevealed || cell.isFlagged) return;
        if (cell.isMine) {
            cell.isRevealed = true;
            explodedRow = row;
            explodedCol = col;
            revealAllMines();
            updateButtons();
            playBombSound();
            smileyButton.setText(":(");
            stopTimer();
            JOptionPane.showMessageDialog(this, "💥 Boom! You hit a mine.", "Game Over", JOptionPane.ERROR_MESSAGE);
            gameInProgress = false;
            continueItem.setEnabled(false);
        } else {
            boardLogic.reveal(row, col);
            updateButtons();
            if (checkWin()) {
                smileyButton.setText("😎");
                stopTimer();
                JOptionPane.showMessageDialog(this, "🎉 You Win!", "Victory", JOptionPane.INFORMATION_MESSAGE);
                gameInProgress = false;
                continueItem.setEnabled(false);
            }
        }
        updateMineCounter();
    }

    private void handleFlagCell(int row, int col) {
        Cell cell = boardLogic.grid[row][col];
        if (cell.isRevealed) return;
        cell.isFlagged = !cell.isFlagged;
        updateButtons();
        updateMineCounter();
    }

    private void updateButtons() {
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                Cell cell = boardLogic.grid[r][c];
                JButton btn = buttons[r][c];
                if (cell.isRevealed) {
                    btn.setEnabled(false);
                    if (cell.isMine) {
                        if (r == explodedRow && c == explodedCol) {
                            btn.setIcon(explodedIcon);
                        } else {
                            btn.setIcon(mineIcon);
                        }
                    } else if (cell.adjacentMines > 0) {
                        btn.setIcon(numberIcons[cell.adjacentMines]);
                    } else {
                        btn.setIcon(numberIcons[0]);
                    }
                } else if (cell.isFlagged) {
                    btn.setIcon(flagIcon);
                    btn.setEnabled(true);
                } else {
                    btn.setIcon(unknownIcon);
                    btn.setEnabled(true);
                }
            }
        }
    }

    private void revealAllMines() {
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                Cell cell = boardLogic.grid[r][c];
                if (cell.isMine) {
                    cell.isRevealed = true;
                }
            }
        }
    }

    private boolean checkWin() {
        for (int r = 0; r < gridRows; r++) {
            for (int c = 0; c < gridCols; c++) {
                Cell cell = boardLogic.grid[r][c];
                if (!cell.isMine && !cell.isRevealed) {
                    return false;
                }
            }
        }
        return true;
    }

    // Call this to ensure timer, counter, and smiley are reset for a new game
    public void startNewGame() {
        gameInProgress = true;
        if (gameTimer != null) gameTimer.stop();
        secondsElapsed = 0;
        if (timerLabel != null) timerLabel.setText("Time: 0");
        if (mineCounterLabel != null) mineCounterLabel.setText("Mines: " + gridMines);
        if (smileyButton != null) smileyButton.setText(":)");
        resetBoard(gridRows, gridCols, gridMines);
    }
}
