package gui;

import game.AudioManager;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * StartScreen - the very first screen the player sees.
 * Shows the game title and a START button.
 * Transitions to MainMenu (mode selection) when clicked.
 */
public class StartScreen extends JFrame {

    private static final Color BG_TOP     = new Color(5, 5, 20);
    private static final Color BG_BOTTOM  = new Color(40, 0, 0);
    private static final Color ACCENT     = new Color(200, 30, 30);
    private static final Color GOLD       = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = new Color(220, 220, 255);
    private static final Color DIM_COLOR  = new Color(150, 150, 180);

    private final Leaderboard leaderboard;
    private Timer blinkTimer;
    private JLabel pressLabel;
    private boolean blinkVisible = true;

    // Stars
    private final int[]   starX     = new int[80];
    private final int[]   starY     = new int[80];
    private final int[]   starSize  = new int[80];
    private final float[] starAlpha = new float[80];

    public StartScreen(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        setTitle("Avengers Disassemble");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        generateStars();
        buildUI();
        startBlinkTimer();
        AudioManager.getInstance().playMusic(AudioManager.MUSIC_MENU);
    }

    private void generateStars() {
        for (int i = 0; i < starX.length; i++) {
            starX[i]    = (int)(Math.random() * 820);
            starY[i]    = (int)(Math.random() * 600);
            starSize[i] = (int)(Math.random() * 3) + 1;
            starAlpha[i]= (float)(Math.random() * 0.8f + 0.2f);
        }
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                // Background gradient
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Diagonal lines
                g2.setColor(new Color(255, 255, 255, 8));
                g2.setStroke(new BasicStroke(1));
                for (int i = -getHeight(); i < getWidth(); i += 35)
                    g2.drawLine(i, 0, i + getHeight(), getHeight());
                // Stars
                for (int i = 0; i < starX.length; i++) {
                    g2.setColor(new Color(1f, 1f, 1f, starAlpha[i]));
                    g2.fillOval(starX[i], starY[i], starSize[i], starSize[i]);
                }
                // Bottom red glow
                g2.setPaint(new RadialGradientPaint(
                        new Point(getWidth()/2, getHeight()),
                        getWidth()/2,
                        new float[]{0f, 1f},
                        new Color[]{new Color(180, 0, 0, 80), new Color(0, 0, 0, 0)}));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(20, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill  = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // Top decorative line
        gbc.gridy = 0;
        root.add(decorativeLine(), gbc);

        // Marvel presents
        JLabel marvelLbl = new JLabel("✦  MARVEL PRESENTS  ✦", SwingConstants.CENTER);
        marvelLbl.setFont(new Font("Arial", Font.BOLD, 13));
        marvelLbl.setForeground(DIM_COLOR);
        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 4, 10);
        root.add(marvelLbl, gbc);

        // AVENGERS
        JLabel titleTop = new JLabel("AVENGERS", SwingConstants.CENTER);
        titleTop.setFont(new Font("Impact", Font.BOLD, 90));
        titleTop.setForeground(ACCENT);
        gbc.gridy = 2; gbc.insets = new Insets(0, 10, 0, 10);
        root.add(titleTop, gbc);

        // DISASSEMBLE
        JLabel titleBot = new JLabel("D I S A S S E M B L E", SwingConstants.CENTER);
        titleBot.setFont(new Font("Impact", Font.PLAIN, 32));
        titleBot.setForeground(GOLD);
        gbc.gridy = 3; gbc.insets = new Insets(0, 10, 6, 10);
        root.add(titleBot, gbc);

        // Divider
        gbc.gridy = 4; gbc.insets = new Insets(0, 80, 16, 80);
        root.add(decorativeLine(), gbc);

        // Game type
        JLabel typeLbl = new JLabel("TURN-BASED STRATEGY GAME", SwingConstants.CENTER);
        typeLbl.setFont(new Font("Arial", Font.BOLD, 12));
        typeLbl.setForeground(DIM_COLOR);
        gbc.gridy = 5; gbc.insets = new Insets(0, 10, 20, 10);
        root.add(typeLbl, gbc);

        // START button
        JButton startBtn = buildStartButton();
        gbc.gridy = 6; gbc.insets = new Insets(0, 200, 16, 200);
        root.add(startBtn, gbc);

        // Blinking label
        pressLabel = new JLabel("- or press any key to continue -", SwingConstants.CENTER);
        pressLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        pressLabel.setForeground(DIM_COLOR);
        gbc.gridy = 7; gbc.insets = new Insets(0, 10, 0, 10);
        root.add(pressLabel, gbc);

        // Team label
        JLabel teamLbl = new JLabel("Group: Fantastic 4  |  v1.0", SwingConstants.CENTER);
        teamLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        teamLbl.setForeground(new Color(100, 100, 130));
        gbc.gridy = 8; gbc.insets = new Insets(20, 10, 0, 10);
        root.add(teamLbl, gbc);

        // Key listener - press any key to start
        root.setFocusable(true);
        root.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { goToMainMenu(); }
        });

        add(root);
        SwingUtilities.invokeLater(root::requestFocusInWindow);
    }

    private JButton buildStartButton() {
        JButton btn = new JButton("START GAME") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(GOLD);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 14, 14);
                g2.setColor(getForeground());
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Impact", Font.PLAIN, 28));
        btn.setBackground(ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(260, 58));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(230, 50, 50)); btn.repaint(); }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(ACCENT); btn.repaint(); }
        });
        btn.addActionListener(e -> goToMainMenu());
        return btn;
    }

    private JPanel decorativeLine() {
        JPanel line = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2;
                g2.setPaint(new GradientPaint(0, 0, new Color(0,0,0,0), cx, 0, ACCENT));
                g2.fillRect(0, getHeight()/2, cx, 2);
                g2.setPaint(new GradientPaint(cx, 0, ACCENT, getWidth(), 0, new Color(0,0,0,0)));
                g2.fillRect(cx, getHeight()/2, cx, 2);
                // Center diamond
                g2.setColor(GOLD);
                int[] xp = {cx, cx+6, cx, cx-6};
                int[] yp = {0, getHeight()/2, getHeight(), getHeight()/2};
                g2.fillPolygon(xp, yp, 4);
            }
        };
        line.setOpaque(false);
        line.setPreferredSize(new Dimension(400, 12));
        return line;
    }

    private void startBlinkTimer() {
        blinkTimer = new Timer(600, e -> {
            blinkVisible = !blinkVisible;
            pressLabel.setVisible(blinkVisible);
        });
        blinkTimer.start();
    }

    private void goToMainMenu() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        if (blinkTimer != null) blinkTimer.stop();
        SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu(leaderboard);
            menu.setVisible(true);
            dispose();
        });
    }
}