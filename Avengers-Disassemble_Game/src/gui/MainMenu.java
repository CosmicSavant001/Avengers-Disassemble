package gui;

import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * MainMenu – Game Mode Selection Screen
 *
 * Modes:
 *   PvP    – Player 1 vs Player 2 (both pick heroes)
 *   PvE    – Player vs AI (player picks, AI randomly picks)
 *   Arcade – Player picks 1 hero, fights all 10 AI heroes in waves
 */
public class MainMenu extends JFrame {

    private final Leaderboard leaderboard;

    private final int[]   starX     = new int[100];
    private final int[]   starY     = new int[100];
    private final int[]   starSize  = new int[100];
    private final float[] starAlpha = new float[100];

    public MainMenu(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
        setTitle("Avengers Disassemble – Select Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(860, 620);
        setLocationRelativeTo(null);
        setResizable(false);
        generateStars();
        buildUI();
    }

    private void generateStars() {
        for (int i = 0; i < starX.length; i++) {
            starX[i]     = (int)(Math.random() * 860);
            starY[i]     = (int)(Math.random() * 620);
            starSize[i]  = (int)(Math.random() * 3) + 1;
            starAlpha[i] = (float)(Math.random() * 0.7f + 0.2f);
        }
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(
                        0, 0, Theme.BG_DARK,
                        getWidth(), getHeight(), Theme.BG_PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());

                for (int i = 0; i < starX.length; i++) {
                    g2.setColor(new Color(1f, 1f, 1f, starAlpha[i]));
                    g2.fillOval(starX[i], starY[i], starSize[i], starSize[i]);
                }

                g2.setPaint(new RadialGradientPaint(
                        new Point(getWidth() / 2, getHeight()),
                        getWidth() / 2f,
                        new float[]{0f, 1f},
                        new Color[]{new Color(255, 107, 157, 60),
                                new Color(0, 0, 0, 0)}));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx  = 0;
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);

        // ── Title ──
        JLabel title = new JLabel("AVENGERS", SwingConstants.CENTER);
        title.setFont(new Font("Impact", Font.BOLD, 52));
        title.setForeground(Theme.GOLD);
        gbc.gridy = 0;
        root.add(title, gbc);

        JLabel sub = new JLabel("D  I  S  A  S  S  E  M  B  L  E",
                SwingConstants.CENTER);
        sub.setFont(new Font("Arial", Font.BOLD, 14));
        sub.setForeground(Theme.COSMIC_BLUE);
        gbc.gridy = 1; gbc.insets = new Insets(0, 10, 6, 10);
        root.add(sub, gbc);

        // ── Divider ──
        gbc.gridy = 2; gbc.insets = new Insets(0, 60, 14, 60);
        root.add(cosmicDivider(), gbc);

        // ── Select Mode label ──
        JLabel modeLbl = new JLabel("— SELECT GAME MODE —", SwingConstants.CENTER);
        modeLbl.setFont(new Font("Arial", Font.BOLD, 12));
        modeLbl.setForeground(Theme.TEXT_DIM);
        gbc.gridy = 3; gbc.insets = new Insets(0, 10, 10, 10);
        root.add(modeLbl, gbc);

        // ── Mode Buttons ──
        gbc.gridy = 4; gbc.insets = new Insets(6, 10, 6, 10);
        root.add(modeCard(
                "⚔  PLAYER  vs  PLAYER",
                "PvP  –  Both players choose their own hero and battle each other",
                new Color(30,  80, 200),
                new Color(60, 120, 255),
                e -> startMode(GameState.GameMode.PVP)
        ), gbc);

        gbc.gridy = 5;
        root.add(modeCard(
                "🤖  PLAYER  vs  AI",
                "PvE  –  You pick a hero. AI randomly picks a hero to fight you",
                new Color(150, 20, 150),
                new Color(200, 50, 200),
                e -> startMode(GameState.GameMode.PVE)
        ), gbc);

        gbc.gridy = 6;
        root.add(modeCard(
                "🏆  ARCADE  MODE",
                "Arcade  –  Pick 1 hero. Survive all 10 AI heroes in a row!",
                new Color(160,  90,  0),
                new Color(220, 140,  0),
                e -> startMode(GameState.GameMode.ARCADE)
        ), gbc);

        // ── Leaderboard ──
        JButton lbBtn = new JButton("🏅  LEADERBOARD");
        lbBtn.setFont(new Font("Arial", Font.BOLD, 12));
        lbBtn.setBackground(Theme.BTN_DARK);
        lbBtn.setForeground(Theme.GOLD);
        lbBtn.setFocusPainted(false);
        lbBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.GOLD, 1),
                new EmptyBorder(7, 20, 7, 20)));
        lbBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbBtn.addActionListener(e -> showLeaderboard());
        gbc.gridy = 7; gbc.insets = new Insets(18, 160, 0, 160);
        root.add(lbBtn, gbc);

        add(root);
    }

    // ── Mode Card ─────────────────────────────────────────────────────────
    private JPanel modeCard(String title, String desc,
                            Color bgNormal, Color bgHover,
                            ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(14, 0)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            }
        };
        card.setBackground(bgNormal);
        card.setOpaque(false);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgHover, 2),
                new EmptyBorder(16, 20, 16, 20)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Impact", Font.PLAIN, 24));
        titleLbl.setForeground(Color.WHITE);

        JLabel descLbl = new JLabel(desc, SwingConstants.RIGHT);
        descLbl.setFont(new Font("Arial", Font.ITALIC, 12));
        descLbl.setForeground(new Color(210, 210, 230));

        card.add(titleLbl, BorderLayout.WEST);
        card.add(descLbl,  BorderLayout.EAST);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(bgHover);  card.repaint(); }
            public void mouseExited(MouseEvent e) {
                card.setBackground(bgNormal); card.repaint(); }
            public void mouseClicked(MouseEvent e) {
                AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
                action.actionPerformed(null);
            }
        });
        return card;
    }

    // ── Cosmic Divider ────────────────────────────────────────────────────
    private JPanel cosmicDivider() {
        JPanel line = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                int cx = getWidth() / 2;
                g2.setPaint(new GradientPaint(0, 0,
                        new Color(0, 0, 0, 0), cx, 0, Theme.COSMIC_BLUE));
                g2.fillRect(0, getHeight() / 2 - 1, cx, 2);
                g2.setPaint(new GradientPaint(cx, 0, Theme.COSMIC_BLUE,
                        getWidth(), 0, new Color(0, 0, 0, 0)));
                g2.fillRect(cx, getHeight() / 2 - 1, cx, 2);
                g2.setColor(Theme.GOLD);
                int[] xp = {cx, cx + 7, cx, cx - 7};
                int[] yp = {0, getHeight() / 2, getHeight(), getHeight() / 2};
                g2.fillPolygon(xp, yp, 4);
            }
        };
        line.setOpaque(false);
        line.setPreferredSize(new Dimension(400, 14));
        return line;
    }

    // ── Navigation ────────────────────────────────────────────────────────
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
        StringBuilder sb = new StringBuilder(
                "═══════════ LEADERBOARD ═══════════\n\n");
        sb.append(String.format("%-4s %-16s %-14s %s%n",
                "#", "Hero", "Mode", "Score"));
        sb.append("─".repeat(46)).append("\n");
        int rank = 1;
        for (var en : scores) {
            sb.append(String.format("%-4d %-16s %-14s %d%n",
                    rank++, en.getUsername(),
                    en.getHeroName(), en.getScore()));
        }
        if (scores.isEmpty()) sb.append("No scores yet!\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(Theme.BG_DARK);
        ta.setForeground(Theme.TEXT_PRIMARY);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(440, 280));
        JOptionPane.showMessageDialog(this, sp, "Leaderboard",
                JOptionPane.PLAIN_MESSAGE);
    }
}