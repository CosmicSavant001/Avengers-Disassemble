package gui;

import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main Menu – shows game title and lets the player choose a game mode.
 * Modes: Player vs AI | Player vs Player | AI vs AI
 */
public class MainMenu extends JFrame {

    private static final Color BG_TOP     = new Color(10, 10, 30);
    private static final Color BG_BOTTOM  = new Color(40, 0, 0);
    private static final Color ACCENT     = new Color(200, 30, 30);
    private static final Color TEXT_COLOR = new Color(220, 220, 255);
    private static final Color GOLD       = new Color(255, 215, 0);
    private static final Font  TITLE_FONT = new Font("Impact", Font.BOLD, 56);
    private static final Font  SUB_FONT   = new Font("Arial",  Font.BOLD, 15);
    private static final Font  BTN_FONT   = new Font("Impact", Font.PLAIN, 22);

    private final Leaderboard leaderboard;

    public MainMenu(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        setTitle("Avengers Disassemble");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(820, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
        AudioManager.getInstance().playMusic(AudioManager.MUSIC_MENU);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, BG_TOP, 0, getHeight(), BG_BOTTOM));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(255, 255, 255, 10));
                g2.setStroke(new BasicStroke(1));
                for (int i = -getHeight(); i < getWidth(); i += 40)
                    g2.drawLine(i, 0, i + getHeight(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel title = new JLabel("AVENGERS", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT);
        gbc.gridy = 0; root.add(title, gbc);

        JLabel sub = new JLabel("D  I  S  A  S  S  E  M  B  L  E", SwingConstants.CENTER);
        sub.setFont(SUB_FONT);
        sub.setForeground(GOLD);
        gbc.gridy = 1; root.add(sub, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(ACCENT);
        gbc.gridy = 2; gbc.insets = new Insets(4, 60, 4, 60);
        root.add(sep, gbc);
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel modeLabel = new JLabel("SELECT GAME MODE", SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 13));
        modeLabel.setForeground(new Color(180, 180, 220));
        gbc.gridy = 3; root.add(modeLabel, gbc);

        gbc.gridy = 4;
        root.add(modeButton("⚔  PLAYER  vs  AI",
            "You control a hero — fight against the computer",
            new Color(160, 20, 20),
            e -> startMode(GameState.GameMode.PLAYER_VS_AI)), gbc);

        gbc.gridy = 5;
        root.add(modeButton("🆚  PLAYER  vs  PLAYER",
            "Two players take turns on the same keyboard",
            new Color(20, 80, 160),
            e -> startMode(GameState.GameMode.PLAYER_VS_PLAYER)), gbc);

        gbc.gridy = 6;
        root.add(modeButton("🤖  AI  vs  AI",
            "Sit back and watch two AIs battle it out",
            new Color(20, 100, 40),
            e -> startMode(GameState.GameMode.AI_VS_AI)), gbc);

        JButton lbBtn = new JButton("🏆  LEADERBOARD");
        lbBtn.setFont(new Font("Arial", Font.BOLD, 13));
        lbBtn.setBackground(new Color(30, 30, 60));
        lbBtn.setForeground(GOLD);
        lbBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GOLD, 1), new EmptyBorder(6, 20, 6, 20)));
        lbBtn.setFocusPainted(false);
        lbBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbBtn.addActionListener(e -> showLeaderboard());
        gbc.gridy = 7; gbc.insets = new Insets(20, 100, 0, 100);
        root.add(lbBtn, gbc);

        add(root);
    }

    private JPanel modeButton(String title, String desc, Color color,
                               java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout(12, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        panel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                new EmptyBorder(14, 20, 14, 20)));
        panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(BTN_FONT);
        titleLbl.setForeground(Color.WHITE);

        JLabel descLbl = new JLabel(desc, SwingConstants.RIGHT);
        descLbl.setFont(new Font("Arial", Font.ITALIC, 12));
        descLbl.setForeground(new Color(200, 200, 200));

        panel.add(titleLbl, BorderLayout.WEST);
        panel.add(descLbl,  BorderLayout.EAST);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(color); panel.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
                panel.repaint(); }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
                action.actionPerformed(null); }
        });
        return panel;
    }

    private void startMode(GameState.GameMode mode) {
        GameState.getInstance().setGameMode(mode);
        SwingUtilities.invokeLater(() -> {
            CharacterSelect cs = new CharacterSelect(this, leaderboard);
            cs.setVisible(true);
            setVisible(false);
        });
    }

    private void showLeaderboard() {
        var scores = leaderboard.getTopScores();
        StringBuilder sb = new StringBuilder("═══════ LEADERBOARD ═══════\n\n");
        sb.append(String.format("%-4s %-15s %-12s %6s%n", "#", "Player", "Hero", "Score"));
        sb.append("─".repeat(42)).append("\n");
        int rank = 1;
        for (var e : scores) {
            sb.append(String.format("%-4d %-15s %-12s %6d%n",
                    rank++, e.getUsername(), e.getHeroName(), e.getScore()));
        }
        if (scores.isEmpty()) sb.append("No scores yet!\n");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(BG_TOP);
        ta.setForeground(TEXT_COLOR);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(420, 260));
        JOptionPane.showMessageDialog(this, sp, "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }
}
