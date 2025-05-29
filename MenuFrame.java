import java.awt.*;
import javax.swing.*;

public class MenuFrame extends JFrame {
    private int rows = 9;
    private int cols = 9;
    private int mines = 10;

    public MenuFrame() {
        setTitle("Minesweeper Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 1, 10, 10));
        setSize(300, 250);
        setLocationRelativeTo(null);

        JButton playButton = new JButton("Play");
        JButton exitButton = new JButton("Exit");
        JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy (9x9, 10)", "Medium (16x16, 40)", "Hard (16x30, 99)"});
        JLabel diffLabel = new JLabel("Select Difficulty:", SwingConstants.CENTER);

        difficultyBox.addActionListener(e -> {
            int idx = difficultyBox.getSelectedIndex();
            if (idx == 0) { rows = 9; cols = 9; mines = 10; }
            else if (idx == 1) { rows = 16; cols = 16; mines = 40; }
            else if (idx == 2) { rows = 16; cols = 30; mines = 99; }
        });

        playButton.addActionListener(e -> {
            // Always start a new game in progress
            MinesweeperFrame frame = new MinesweeperFrame(rows, cols, mines);
            frame.setVisible(true);
            frame.startNewGame(); // Ensure timer, counter, and smiley are reset
            dispose();
        });
        exitButton.addActionListener(e -> System.exit(0));

        add(diffLabel);
        add(difficultyBox);
        add(playButton);
        add(exitButton);
    }
}
