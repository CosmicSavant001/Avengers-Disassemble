package gui;

import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Game Over / Victory screen displayed at the end of a run.
 * Shows the player's final score, saves to the leaderboard, and
 * offers options to play again or return to the main menu.
 */
public class GameOverScreen extends JFrame {

    private static final Color BG_COLOR    = new Color(10, 10, 30);
    private static final Color ACCENT      = new Color(200, 30, 30);
    private static final Color GOLD_COLOR  = new Color(255, 215, 0);
    private static final Color TEXT_COLOR  = new Color(220, 220, 255);

    private final JFrame     mainMenuRef;
    private final Leaderboard leaderboard;
    private final String     username;
    private final String     heroName;
    private final int        score;
    private final boolean    playerWon;

    public GameOverScreen(JFrame mainMenuRef, Leaderboard leaderboard,
                          String username, String heroName, int score, boolean playerWon) {
        this.mainMenuRef = mainMenuRef;
        this.leaderboard  = leaderboard;
        this.username     = username;
        this.heroName     = heroName;
        this.score        = score;
        this.playerWon    = playerWon;

        setTitle(playerWon ? "Victory!" : "Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Save score to leaderboard
        if (score > 0) {
            leaderboard.submitScore(username, score, heroName);
        }

        buildUI();
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, playerWon ? new Color(10, 40, 10) : new Color(40, 5, 5),
                        getWidth(), getHeight(), BG_COLOR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.insets = new Insets(12, 10, 12, 10);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // ── Result Banner ──
        String bannerText = playerWon ? "🏆 VICTORY!" : "💀 GAME OVER";
        JLabel banner = new JLabel(bannerText, SwingConstants.CENTER);
        banner.setFont(new Font("Impact", Font.BOLD, 56));
        banner.setForeground(playerWon ? GOLD_COLOR : ACCENT);
        gbc.gridy = 0;
        root.add(banner, gbc);

        // ── Flavour text ──
        String flavour = playerWon
                ? username + " saved the world as " + heroName + "!"
                : heroName + " fought bravely but was defeated.";
        JLabel flvLbl = new JLabel("<html><center>" + flavour + "</center></html>", SwingConstants.CENTER);
        flvLbl.setForeground(TEXT_COLOR);
        flvLbl.setFont(new Font("Arial", Font.ITALIC, 15));
        gbc.gridy = 1;
        root.add(flvLbl, gbc);

        // ── Stats Panel ──
        JPanel stats = buildStatsPanel();
        gbc.gridy = 2;
        root.add(stats, gbc);

        // ── Leaderboard rank ──
        int bestScore = leaderboard.getBestScore(username);
        JLabel lbInfo = new JLabel(
                "Your best score: " + bestScore, SwingConstants.CENTER);
        lbInfo.setForeground(GOLD_COLOR);
        lbInfo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy = 3;
        root.add(lbInfo, gbc);

        // ── Buttons ──
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);
        btnRow.add(styledButton("Play Again",   new Color(30, 100, 30), e -> playAgain()));
        btnRow.add(styledButton("Main Menu",    new Color(60, 60, 130), e -> goToMainMenu()));
        btnRow.add(styledButton("Leaderboard",  new Color(120, 80, 0),  e -> showLeaderboard()));
        gbc.gridy = 4;
        root.add(btnRow, gbc);

        add(root);
    }

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 6));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                new EmptyBorder(10, 20, 10, 20)
        ));

        addStat(panel, "Final Score",       String.valueOf(score));
        addStat(panel, "Hero Used",         heroName);
        addStat(panel, "Battles Won",       String.valueOf(GameState.getInstance().getBattlesWon()));
        addStat(panel, "Enemies Defeated",  String.valueOf(GameState.getInstance().getEnemiesDefeated()));
        addStat(panel, "Battles Lost",      String.valueOf(GameState.getInstance().getBattlesLost()));
        addStat(panel, "Player",            username);

        return panel;
    }

    private void addStat(JPanel panel, String key, String value) {
        JLabel k = new JLabel(key + ":", SwingConstants.RIGHT);
        k.setForeground(new Color(180, 180, 220));
        k.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel v = new JLabel(value, SwingConstants.LEFT);
        v.setForeground(TEXT_COLOR);
        v.setFont(new Font("Arial", Font.BOLD, 13));

        panel.add(k);
        panel.add(v);
    }

    private JButton styledButton(String text, Color bg, java.awt.event.ActionListener l) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.brighter(), 2),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(l);
        return btn;
    }

    // ─── Navigation ──────────────────────────────────────────────────────

    private void playAgain() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        GameState.getInstance().resetRunData();
        SwingUtilities.invokeLater(() -> {
            CharacterSelect cs = new CharacterSelect(mainMenuRef, leaderboard);
            cs.setVisible(true);
            dispose();
        });
    }

    private void goToMainMenu() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        GameState.getInstance().resetRunData();
        SwingUtilities.invokeLater(() -> {
            mainMenuRef.setVisible(true);
            dispose();
        });
    }

    private void showLeaderboard() {
        java.util.List<Leaderboard.Entry> scores = leaderboard.getTopScores();
        StringBuilder sb = new StringBuilder("═══ TOP SCORES ═══\n\n");
        sb.append(String.format("%-4s %-15s %-12s %s%n", "#", "Player", "Hero", "Score"));
        sb.append("─".repeat(40)).append("\n");
        int rank = 1;
        for (Leaderboard.Entry e : scores) {
            sb.append(String.format("%-4d %-15s %-12s %d%n",
                    rank++, e.getUsername(), e.getHeroName(), e.getScore()));
        }
        if (scores.isEmpty()) sb.append("No scores yet!\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(BG_COLOR);
        ta.setForeground(TEXT_COLOR);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(380, 260));
        JOptionPane.showMessageDialog(this, sp, "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }
}
