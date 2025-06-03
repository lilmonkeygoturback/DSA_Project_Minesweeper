import java.awt.*;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MenuFrame extends JFrame {
    private int rows = 9;
    private int cols = 9;
    private int mines = 10;
    private Clip themeClip;

    public MenuFrame() {
        setTitle("Minesweeper Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        // Create a main panel with custom background color
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gradient background
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(30, 30, 30);
                Color color2 = new Color(60, 60, 90);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Game title label
        JLabel titleLabel = new JLabel("MINESWEEPER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Difficulty label
        JLabel diffLabel = new JLabel("Select Difficulty:", SwingConstants.CENTER);
        diffLabel.setFont(new Font("Arial", Font.BOLD, 18));
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diffLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy (9x9, 10)", "Medium (16x16, 40)", "Hard (16x30, 99)"});
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 16));
        difficultyBox.setMaximumSize(new Dimension(200, 30));
        difficultyBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultyBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                switch (difficultyBox.getSelectedIndex()) {
                    case 0 -> { rows = 9; cols = 9; mines = 10; }
                    case 1 -> { rows = 16; cols = 16; mines = 40; }
                    case 2 -> { rows = 16; cols = 30; mines = 99; }
                }
            }
        });

        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 20));
        playButton.setBackground(new Color(34, 139, 34));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 0), 2),
            BorderFactory.createEmptyBorder(10, 40, 10, 40)));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                // Lower volume and pass themeClip to game
                lowerThemeVolume();
                MinesweeperFrame frame = new MinesweeperFrame(rows, cols, mines, themeClip);
                frame.setVisible(true);
                frame.startNewGame();
                dispose();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setBackground(new Color(139, 0, 0));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 0, 0), 2),
            BorderFactory.createEmptyBorder(8, 40, 8, 40)));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                stopThemeMusic();
                System.exit(0);
            }
        });

        // Add spacing and components
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(diffLabel);
        mainPanel.add(difficultyBox);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(playButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(exitButton);

        setContentPane(mainPanel);
        playThemeMusic();
    }

    private void playThemeMusic() {
        try {
            File soundFile = new File("assets/sound/pixel-dreams-259187.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            themeClip = AudioSystem.getClip();
            themeClip.open(audioIn);
            themeClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.err.println("Error playing theme music: " + ex.getMessage());
        }
    }

    private void lowerThemeVolume() {
        if (themeClip != null) {
            FloatControl gainControl = (FloatControl) themeClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f); // Lower volume by 15 decibels
        }
    }

    public static void restoreThemeVolume(Clip clip) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(0.0f); // Restore to normal volume
        }
    }

    public Clip getThemeClip() {
        return themeClip;
    }

    private void stopThemeMusic() {
        if (themeClip != null && themeClip.isRunning()) {
            themeClip.stop();
            themeClip.close();
        }
    }
}
