package gui;

import characters.*;
import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Character selection screen.
 * - PvAI:  Player 1 picks one hero → goes to battle vs AI
 * - PvP:   Player 1 picks, then Player 2 picks → both go to battle
 * - AIvAI: Auto-picks two random heroes → goes straight to battle
 */
public class CharacterSelect extends JFrame {

    private static final Color BG_COLOR    = new Color(10, 10, 30);
    private static final Color ACCENT      = new Color(200, 30, 30);
    private static final Color TEXT_COLOR  = new Color(220, 220, 255);
    private static final Color CARD_BG     = new Color(20, 20, 60);
    private static final Color SEL_BG      = new Color(50, 10, 10);
    private static final Color SEL_BORDER  = new Color(255, 200, 0);

    private final JFrame      parentFrame;
    private final Leaderboard leaderboard;

    private final List<Hero>   heroes    = new ArrayList<>();
    private Hero               selected  = null;
    private final List<JPanel> heroCards = new ArrayList<>();

    private int    pickingForPlayer = 1; // 1 or 2
    private JLabel titleLabel;
    private JLabel descLabel;
    private JLabel statsLabel;
    private JButton confirmBtn;

    public CharacterSelect(JFrame parent, Leaderboard leaderboard) {
        this.parentFrame = parent;
        this.leaderboard = leaderboard;

        setTitle("Choose Your Hero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setResizable(false);

        buildHeroes();

        // AI vs AI — skip selection entirely
        if (GameState.getInstance().isAIvAI()) {
            autoPickForAIvAI();
        } else {
            buildUI();
        }
    }

    private void buildHeroes() {
        heroes.add(new IronMan());
        heroes.add(new CaptainAmerica());
        heroes.add(new Thor());
        heroes.add(new Hulk());
        heroes.add(new BlackWidow());
    }

    // ── AI vs AI: auto-pick two different heroes ────────────────────────
    private void autoPickForAIvAI() {
        GameState.getInstance().setSelectedHero(heroes.get(0));  // Iron Man
        GameState.getInstance().setSelectedHero2(heroes.get(2)); // Thor
        openBattleScreen();
    }

    // ─── UI ──────────────────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(BG_COLOR);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title changes based on mode & turn
        titleLabel = new JLabel(getPickTitle(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 30));
        titleLabel.setForeground(ACCENT);
        root.add(titleLabel, BorderLayout.NORTH);

        // Hero cards
        JPanel cardsPanel = new JPanel(new GridLayout(1, 5, 12, 0));
        cardsPanel.setBackground(BG_COLOR);
        for (Hero h : heroes) {
            JPanel card = buildHeroCard(h);
            heroCards.add(card);
            cardsPanel.add(card);
        }
        root.add(cardsPanel, BorderLayout.CENTER);

        // Bottom info + confirm
        JPanel infoRow = new JPanel(new BorderLayout(20, 0));
        infoRow.setBackground(BG_COLOR);

        descLabel = new JLabel("<html><i>Click a hero to learn more.</i></html>");
        descLabel.setForeground(TEXT_COLOR);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        descLabel.setPreferredSize(new Dimension(500, 70));

        statsLabel = new JLabel("", SwingConstants.RIGHT);
        statsLabel.setForeground(new Color(180, 220, 255));
        statsLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));

        infoRow.add(descLabel,  BorderLayout.CENTER);
        infoRow.add(statsLabel, BorderLayout.EAST);

        confirmBtn = buildButton("⚔  CONFIRM", e -> handleConfirm());
        confirmBtn.setEnabled(false);
        confirmBtn.setFont(new Font("Impact", Font.BOLD, 20));
        confirmBtn.setPreferredSize(new Dimension(160, 50));

        JPanel bottomRow = new JPanel(new BorderLayout(10, 0));
        bottomRow.setBackground(BG_COLOR);
        bottomRow.add(infoRow,    BorderLayout.CENTER);
        bottomRow.add(confirmBtn, BorderLayout.EAST);

        root.add(bottomRow, BorderLayout.SOUTH);
        add(root);
        setVisible(true);
    }

    // ─── Hero Card ────────────────────────────────────────────────────────

    private JPanel buildHeroCard(Hero hero) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 100), 2),
                new EmptyBorder(10, 8, 10, 8)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Portrait image or coloured fallback
        java.awt.image.BufferedImage heroImg = null;
        try {
            java.io.File f = new java.io.File(hero.getImagePath());
            if (f.exists()) heroImg = javax.imageio.ImageIO.read(f);
        } catch (Exception ignored) {}
        final java.awt.image.BufferedImage finalImg = heroImg;
        final Color hColor = heroColor(hero);

        JPanel portrait = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                if (finalImg != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(finalImg, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setColor(hColor);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    g2.setFont(new Font("Impact", Font.PLAIN, 28));
                    FontMetrics fm = g2.getFontMetrics();
                    String ini = getInitials(hero.getName());
                    g2.drawString(ini, (getWidth()-fm.stringWidth(ini))/2,
                            getHeight()/2 + fm.getAscent()/3);
                }
            }
        };
        portrait.setPreferredSize(new Dimension(110, 120));
        card.add(portrait, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(hero.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setFont(new Font("Impact", Font.PLAIN, 14));
        card.add(nameLabel, BorderLayout.NORTH);

        JPanel statBar = new JPanel(new GridLayout(3, 1, 2, 2));
        statBar.setBackground(CARD_BG);
        statBar.add(miniStat("HP:  " + hero.getMaxHealth(),    new Color(80, 200, 80)));
        statBar.add(miniStat("ATK: " + hero.getAttackPower(),  new Color(200, 80, 80)));
        statBar.add(miniStat("DEF: " + hero.getDefensePower(), new Color(80, 80, 200)));
        card.add(statBar, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectHero(hero, card);
                AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (selected != hero) card.setBackground(new Color(30, 30, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (selected != hero) card.setBackground(CARD_BG);
            }
        });
        return card;
    }

    private void selectHero(Hero hero, JPanel clickedCard) {
        selected = hero;
        for (JPanel c : heroCards) {
            c.setBackground(CARD_BG);
            c.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(60, 60, 100), 2),
                    new EmptyBorder(10, 8, 10, 8)));
        }
        clickedCard.setBackground(SEL_BG);
        clickedCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SEL_BORDER, 3),
                new EmptyBorder(10, 8, 10, 8)));

        descLabel.setText("<html>" + hero.getDescription() + "</html>");
        String ab = hero.getSpecialAbility() != null ? hero.getSpecialAbility().getName() : "None";
        statsLabel.setText(String.format(
                "<html><b>HP:</b> %d &nbsp; <b>ATK:</b> %d &nbsp; <b>DEF:</b> %d<br/><b>Ability:</b> %s</html>",
                hero.getMaxHealth(), hero.getAttackPower(), hero.getDefensePower(), ab));
        confirmBtn.setEnabled(true);
        revalidate(); repaint();
    }

    // ─── Confirm / Navigation ─────────────────────────────────────────────

    private void handleConfirm() {
        if (selected == null) return;
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);

        if (pickingForPlayer == 1) {
            GameState.getInstance().setSelectedHero(selected);

            if (GameState.getInstance().isPvP()) {
                // PvP: ask Player 2 to pick
                pickingForPlayer = 2;
                selected = null;
                confirmBtn.setEnabled(false);
                titleLabel.setText(getPickTitle());
                // Deselect all cards visually
                for (JPanel c : heroCards) {
                    c.setBackground(CARD_BG);
                    c.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(60, 60, 100), 2),
                            new EmptyBorder(10, 8, 10, 8)));
                }
                descLabel.setText("<html><i>Player 2 — click a hero to learn more.</i></html>");
                statsLabel.setText("");
                revalidate(); repaint();
            } else {
                // PvAI: only one pick needed
                openBattleScreen();
            }

        } else {
            // Player 2 confirmed
            GameState.getInstance().setSelectedHero2(selected);
            openBattleScreen();
        }
    }

    private void openBattleScreen() {
        SwingUtilities.invokeLater(() -> {
            BattleScreen bs = new BattleScreen(parentFrame, leaderboard);
            bs.setVisible(true);
            dispose();
        });
    }

    // ─── Helpers ─────────────────────────────────────────────────────────

    private String getPickTitle() {
        if (GameState.getInstance().isPvP()) {
            return pickingForPlayer == 1
                    ? "PLAYER 1 — CHOOSE YOUR HERO"
                    : "PLAYER 2 — CHOOSE YOUR HERO";
        }
        return "SELECT YOUR HERO";
    }

    private JLabel miniStat(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setForeground(color);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 11));
        return lbl;
    }

    private JButton buildButton(String text, java.awt.event.ActionListener l) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(160, 10, 10));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2), new EmptyBorder(8, 14, 8, 14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(l);
        return btn;
    }

    private Color heroColor(Hero h) {
        if (h instanceof IronMan)        return new Color(180, 30, 30);
        if (h instanceof CaptainAmerica) return new Color(30, 60, 180);
        if (h instanceof Thor)           return new Color(80, 80, 180);
        if (h instanceof Hulk)           return new Color(40, 150, 40);
        if (h instanceof BlackWidow)     return new Color(60, 10, 80);
        return Color.GRAY;
    }

    private String getInitials(String name) {
        StringBuilder sb = new StringBuilder();
        for (String p : name.split(" ")) if (!p.isEmpty()) sb.append(p.charAt(0));
        return sb.toString().toUpperCase();
    }
}
