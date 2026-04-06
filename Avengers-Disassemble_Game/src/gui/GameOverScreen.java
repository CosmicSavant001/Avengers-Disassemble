package gui;

import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * GameOverScreen – shown after a battle ends.
 *
 * Displays:
 *  - WIN or LOSE banner
 *  - Final stats (score, battles won, hero used, mode)
 *  - RESTART button  → goes back to Character Select
 *  - MAIN MENU button → goes back to MainMenu (game mode screen)
 */
public class GameOverScreen extends JFrame {

    // ── Colours ───────────────────────────────────────────────────────────
    private static final Color BG_COLOR   = new Color(10, 10, 30);
    private static final Color ACCENT     = new Color(200, 30, 30);
    private static final Color GOLD       = new Color(255, 215, 0);
    private static final Color TEXT_COLOR = new Color(220, 220, 255);
    private static final Color GREEN      = new Color(50, 200, 80);

    private final JFrame      mainMenuRef;
    private final Leaderboard leaderboard;
    private final String      heroName;
    private final int         score;
    private final boolean     playerWon;

    public GameOverScreen(JFrame mainMenuRef, Leaderboard leaderboard,
                          String heroName, int score, boolean playerWon) {
        this.mainMenuRef = mainMenuRef;
        this.leaderboard = leaderboard;
        this.heroName    = heroName;
        this.score       = score;
        this.playerWon   = playerWon;

        setTitle(playerWon ? "Victory!" : "Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(680, 560);
        setLocationRelativeTo(null);
        setResizable(false);

        // Save score to leaderboard
        if (score > 0) {
            String mode = GameState.getInstance().getGameMode().toString();
            leaderboard.submitScore(heroName, score, mode);
        }

        buildUI();
        AudioManager.getInstance().stopMusic();
    }

    // ── UI ────────────────────────────────────────────────────────────────
    private void buildUI() {
        // Gradient background
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                Color top = playerWon ? new Color(5, 30, 5) : new Color(30, 5, 5);
                g2.setPaint(new GradientPaint(0, 0, top, 0, getHeight(), BG_COLOR));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Decorative diagonal lines
                g2.setColor(new Color(255, 255, 255, 6));
                g2.setStroke(new BasicStroke(1));
                for (int i = -getHeight(); i < getWidth(); i += 40)
                    g2.drawLine(i, 0, i + getHeight(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // ── WIN / LOSE Banner ──
        String bannerText = playerWon ? "🏆  VICTORY!" : "💀  DEFEAT";
        JLabel banner = new JLabel(bannerText, SwingConstants.CENTER);
        banner.setFont(new Font("Impact", Font.BOLD, 64));
        banner.setForeground(playerWon ? GOLD : ACCENT);
        gbc.gridy = 0;
        root.add(banner, gbc);

        // ── Flavour text ──
        String flavour = playerWon
                ? heroName + " has proven their worth in the arena!"
                : heroName + " has been defeated by the clone army.";
        JLabel flavourLbl = new JLabel(
                "<html><center>" + flavour + "</center></html>",
                SwingConstants.CENTER);
        flavourLbl.setFont(new Font("Arial", Font.ITALIC, 14));
        flavourLbl.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        root.add(flavourLbl, gbc);

        // ── Divider ──
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(60, 60, 100));
        gbc.gridy = 2; gbc.insets = new Insets(4, 20, 4, 20);
        root.add(sep, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);

        // ── Stats Panel ──
        gbc.gridy = 3;
        root.add(buildStatsPanel(), gbc);

        // ── Divider ──
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(60, 60, 100));
        gbc.gridy = 4; gbc.insets = new Insets(4, 20, 16, 20);
        root.add(sep2, gbc);
        gbc.insets = new Insets(8, 10, 8, 10);

        // ── Action Buttons ──
        gbc.gridy = 5;
        root.add(buildButtonPanel(), gbc);

        add(root);
    }

    // ── Stats panel ───────────────────────────────────────────────────────
    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 4, 16, 10));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 80), 1),
                new EmptyBorder(14, 20, 14, 20)));

        String modeText = switch (GameState.getInstance().getGameMode()) {
            case PVE    -> "Player vs AI";
            case PVP    -> "Player vs Player";
            case ARCADE -> "Arcade";
        };

        addStatBox(panel, "SCORE",    String.valueOf(score),                          GOLD);
        addStatBox(panel, "HERO",     heroName,                                       new Color(100, 180, 255));
        addStatBox(panel, "MODE",     modeText,                                       new Color(180, 100, 255));
        addStatBox(panel, "RESULT",   playerWon ? "WIN" : "LOSE",                    playerWon ? GREEN : ACCENT);
        addStatBox(panel, "BATTLES WON",  String.valueOf(GameState.getInstance().getBattlesWon()),      GREEN);
        addStatBox(panel, "ENEMIES",  String.valueOf(GameState.getInstance().getEnemiesDefeated()),    new Color(255, 140, 0));
        addStatBox(panel, "BEST SCORE", String.valueOf(leaderboard.getBestScore(heroName)),            GOLD);
        addStatBox(panel, "RANK",     getRankText(),                                  TEXT_COLOR);

        return panel;
    }

    private void addStatBox(JPanel panel, String label, String value, Color valueColor) {
        JPanel box = new JPanel(new BorderLayout(2, 2));
        box.setBackground(new Color(15, 15, 40));
        box.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 70), 1),
                new EmptyBorder(6, 8, 6, 8)));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 10));
        lbl.setForeground(new Color(140, 140, 170));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Impact", Font.PLAIN, 18));
        val.setForeground(valueColor);

        box.add(lbl, BorderLayout.NORTH);
        box.add(val, BorderLayout.CENTER);
        panel.add(box);
    }

    // ── Buttons panel ─────────────────────────────────────────────────────
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setOpaque(false);

        // RESTART – go back to character select
        JButton restartBtn = buildActionButton(
                "🔄  RESTART",
                "Pick heroes again",
                new Color(30, 100, 30),
                new Color(50, 180, 50),
                e -> handleRestart()
        );

        // MAIN MENU – go back to game mode selection
        JButton menuBtn = buildActionButton(
                "🏠  MAIN MENU",
                "Back to mode select",
                new Color(30, 30, 100),
                new Color(60, 60, 200),
                e -> handleMainMenu()
        );

        panel.add(restartBtn);
        panel.add(menuBtn);
        return panel;
    }

    private JButton buildActionButton(String title, String subtitle,
                                       Color bgNormal, Color bgHover,
                                       java.awt.event.ActionListener action) {
        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Title text
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Impact", Font.PLAIN, 22));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(title,
                        (getWidth() - fm.stringWidth(title)) / 2,
                        getHeight() / 2 - 2);

                // Subtitle text
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                fm = g2.getFontMetrics();
                g2.setColor(new Color(200, 200, 200));
                g2.drawString(subtitle,
                        (getWidth() - fm.stringWidth(subtitle)) / 2,
                        getHeight() / 2 + 16);
            }
        };
        btn.setBackground(bgNormal);
        btn.setPreferredSize(new Dimension(200, 70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bgHover); btn.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgNormal); btn.repaint(); }
        });
        btn.addActionListener(action);
        return btn;
    }

    // ── Navigation ────────────────────────────────────────────────────────

    /** Restart: reset run data and go back to Character Select */
    private void handleRestart() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        GameState.getInstance().resetRunData();
        SwingUtilities.invokeLater(() -> {
            CharacterSelect cs = new CharacterSelect(mainMenuRef, leaderboard);
            cs.setVisible(true);
            dispose();
        });
    }

    /** Main Menu: reset run data and go back to mode selection */
    private void handleMainMenu() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        GameState.getInstance().resetRunData();
        SwingUtilities.invokeLater(() -> {
            mainMenuRef.setVisible(true);
            dispose();
        });
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private String getRankText() {
        var scores = leaderboard.getTopScores();
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).getScore() == score
                    && scores.get(i).getUsername().equals(heroName)) {
                return "#" + (i + 1);
            }
        }
        return "—";
    }
}
