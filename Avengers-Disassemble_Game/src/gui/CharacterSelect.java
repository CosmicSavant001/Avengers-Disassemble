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
 * CharacterSelect - Hero selection screen for all 3 game modes.
 * Cosmic Marvel colour scheme.
 *
 * PvP    : Player 1 Blue picks first, then Player 2 Red picks
 * PvE    : Player picks, AI randomly picks opponent
 * Arcade : Player picks 1 hero, fights all 10 AI heroes in waves
 */
public class CharacterSelect extends JFrame {

    private final JFrame      parentFrame;
    private final Leaderboard leaderboard;

    private final List<Hero>   heroes    = new ArrayList<>();
    private Hero               selected  = null;
    private final List<JPanel> heroCards = new ArrayList<>();
    private int                pickingForPlayer = 1;

    private JLabel  titleLabel;
    private JLabel  playerBadge;
    private JLabel  descLabel;
    private JLabel  statsLabel;
    private JButton confirmBtn;

    public CharacterSelect(JFrame parent, Leaderboard leaderboard) {
        this.parentFrame = parent;
        this.leaderboard = leaderboard;
        setTitle("Avengers Disassemble - Choose Hero");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 660);
        setLocationRelativeTo(null);
        setResizable(false);
        buildHeroes();
        buildUI();
    }

    private void buildHeroes() {
        heroes.add(new IronMan());
        heroes.add(new SpiderMan());
        heroes.add(new Thor());
        heroes.add(new Hulk());
        heroes.add(new BlackWidow());
        heroes.add(new ScarletWitch());
        heroes.add(new DoctorStrange());
        heroes.add(new Cadie());
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, Theme.BG_DARK,
                        getWidth(), getHeight(), Theme.BG_PRIMARY));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setBorder(new EmptyBorder(14, 14, 14, 14));
        root.add(buildTopBanner(),  BorderLayout.NORTH);
        root.add(buildCardsPanel(), BorderLayout.CENTER);
        root.add(buildBottomBar(),  BorderLayout.SOUTH);
        add(root);
    }

    private JPanel buildTopBanner() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 0, 8, 0));

        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 12));
        back.setBackground(Theme.BTN_DARK);
        back.setForeground(Theme.TEXT_PRIMARY);
        back.setFocusPainted(false);
        back.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 140), 1),
                new EmptyBorder(6, 14, 6, 14)));
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        back.addActionListener(e -> { parentFrame.setVisible(true); dispose(); });

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 4, 0);

        titleLabel = new JLabel(getTitleText(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Impact", Font.BOLD, 26));
        titleLabel.setForeground(Theme.GOLD);
        center.add(titleLabel, gbc);

        gbc.gridy = 1;
        playerBadge = new JLabel(getBadgeText(), SwingConstants.CENTER);
        playerBadge.setFont(new Font("Arial", Font.BOLD, 13));
        playerBadge.setForeground(getBadgeColor());
        playerBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBadgeColor(), 2),
                new EmptyBorder(3, 12, 3, 12)));
        center.add(playerBadge, gbc);

        panel.add(back,   BorderLayout.WEST);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildCardsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, heroes.size(), 10, 0));
        panel.setOpaque(false);
        for (Hero h : heroes) {
            JPanel card = buildCard(h);
            heroCards.add(card);
            panel.add(card);
        }
        return panel;
    }

    private JPanel buildCard(Hero hero) {
        JPanel card = new JPanel(new BorderLayout(4, 6));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 100), 2),
                new EmptyBorder(8, 6, 8, 6)));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        java.awt.image.BufferedImage img = null;
        try {
            java.io.File f = new java.io.File(hero.getImagePath());
            if (f.exists()) img = javax.imageio.ImageIO.read(f);
        } catch (Exception ignored) {}
        final java.awt.image.BufferedImage portrait = img;

        JPanel imgPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                if (portrait != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                    g2.drawImage(portrait, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setPaint(new GradientPaint(0, 0, Theme.BG_PRIMARY,
                            0, getHeight(), Theme.BG_DARK));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Theme.COSMIC_BLUE);
                    g2.setFont(new Font("Impact", Font.PLAIN, 22));
                    FontMetrics fm = g2.getFontMetrics();
                    String ini = getInitials(hero.getName());
                    g2.drawString(ini,
                            (getWidth() - fm.stringWidth(ini)) / 2,
                            getHeight() / 2 + fm.getAscent() / 3);
                }
            }
        };
        imgPanel.setPreferredSize(new Dimension(100, 110));
        imgPanel.setOpaque(false);

        JLabel nameLbl = new JLabel(hero.getName(), SwingConstants.CENTER);
        nameLbl.setFont(new Font("Impact", Font.PLAIN, 13));
        nameLbl.setForeground(Theme.TEXT_PRIMARY);

        JPanel statBar = new JPanel(new GridLayout(3, 1, 1, 1));
        statBar.setOpaque(false);
        statBar.add(miniStat("HP  " + hero.getMaxHealth(),    Theme.HP_HERO));
        statBar.add(miniStat("ATK " + hero.getAttackPower(),  Theme.NEBULA_PINK));
        statBar.add(miniStat("DEF " + hero.getDefensePower(), Theme.COSMIC_BLUE));

        card.add(nameLbl,  BorderLayout.NORTH);
        card.add(imgPanel, BorderLayout.CENTER);
        card.add(statBar,  BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectHero(hero, card);
                AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (selected != hero) { card.setBackground(Theme.BG_PRIMARY); card.repaint(); }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (selected != hero) { card.setBackground(Theme.BG_CARD); card.repaint(); }
            }
        });
        return card;
    }

    private JPanel buildBottomBar() {
        JPanel panel = new JPanel(new BorderLayout(16, 0));
        panel.setBackground(Theme.BG_PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(40, 40, 80), 1),
                new EmptyBorder(10, 14, 10, 14)));

        descLabel = new JLabel("<html><i>Click a hero to see their story.</i></html>");
        descLabel.setForeground(Theme.TEXT_DIM);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setPreferredSize(new Dimension(500, 55));

        statsLabel = new JLabel("", SwingConstants.RIGHT);
        statsLabel.setForeground(Theme.COSMIC_BLUE);
        statsLabel.setFont(new Font("Monospaced", Font.PLAIN, 11));

        confirmBtn = new JButton("CONFIRM") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? getBadgeColor() : new Color(40, 40, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Impact", Font.PLAIN, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        confirmBtn.setContentAreaFilled(false);
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setPreferredSize(new Dimension(160, 48));
        confirmBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        confirmBtn.setEnabled(false);
        confirmBtn.addActionListener(e -> handleConfirm());

        JPanel infoPanel = new JPanel(new BorderLayout(8, 0));
        infoPanel.setOpaque(false);
        infoPanel.add(descLabel,  BorderLayout.CENTER);
        infoPanel.add(statsLabel, BorderLayout.EAST);

        panel.add(infoPanel,  BorderLayout.CENTER);
        panel.add(confirmBtn, BorderLayout.EAST);
        return panel;
    }

    private void selectHero(Hero hero, JPanel clicked) {
        selected = hero;
        Color selColor = (pickingForPlayer == 2) ? Theme.RED_TEAM : Theme.BLUE_TEAM;
        for (JPanel c : heroCards) {
            c.setBackground(Theme.BG_CARD);
            c.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 50, 100), 2),
                    new EmptyBorder(8, 6, 8, 6)));
        }
        clicked.setBackground(new Color(10, 15, 50));
        clicked.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(selColor, 3),
                new EmptyBorder(8, 6, 8, 6)));
        descLabel.setText("<html>" + hero.getDescription() + "</html>");
        String ab = hero.getSpecialAbility() != null
                ? hero.getSpecialAbility().getName() : "None";
        statsLabel.setText(String.format(
                "<html><b>HP:</b> %d &nbsp;<b>ATK:</b> %d &nbsp;<b>DEF:</b> %d"
                + " &nbsp;<b>MP:</b> %d<br/><b>Skill:</b> %s</html>",
                hero.getMaxHealth(), hero.getAttackPower(),
                hero.getDefensePower(), hero.getMaxMana(), ab));
        confirmBtn.setEnabled(true);
        confirmBtn.repaint();
        revalidate(); repaint();
    }

    private void handleConfirm() {
        if (selected == null) return;
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        GameState gs = GameState.getInstance();
        if (gs.isPvP()) {
            if (pickingForPlayer == 1) {
                gs.setSelectedHero(selected);
                switchToPlayer2();
            } else {
                gs.setSelectedHero2(selected);
                openBattle();
            }
        } else {
            gs.setSelectedHero(selected);
            openBattle();
        }
    }

    private void switchToPlayer2() {
        pickingForPlayer = 2;
        selected = null;
        confirmBtn.setEnabled(false);
        for (JPanel c : heroCards) {
            c.setBackground(Theme.BG_CARD);
            c.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 50, 100), 2),
                    new EmptyBorder(8, 6, 8, 6)));
        }
        titleLabel.setText(getTitleText());
        titleLabel.setForeground(Theme.NEBULA_PINK);
        playerBadge.setText(getBadgeText());
        playerBadge.setForeground(Theme.RED_TEAM);
        playerBadge.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.RED_TEAM, 2),
                new EmptyBorder(3, 12, 3, 12)));
        descLabel.setText("<html><i>Player 2 - click a hero to see their story.</i></html>");
        statsLabel.setText("");
        confirmBtn.repaint();
        JOptionPane.showMessageDialog(this,
                "Player 1 locked in!\n\nPlayer 2 - now choose your hero.",
                "Player 2's Turn", JOptionPane.INFORMATION_MESSAGE);
        revalidate(); repaint();
    }

    private void openBattle() {
        SwingUtilities.invokeLater(() -> {
            BattleScreen bs = new BattleScreen(parentFrame, leaderboard);
            bs.setVisible(true);
            dispose();
        });
    }

    private String getTitleText() {
        GameState gs = GameState.getInstance();
        if (gs.isPvP()) return pickingForPlayer == 1
                ? "PLAYER 1 - CHOOSE YOUR HERO"
                : "PLAYER 2 - CHOOSE YOUR HERO";
        if (gs.isArcade()) return "ARCADE - CHOOSE YOUR HERO";
        return "CHOOSE YOUR HERO";
    }

    private String getBadgeText() {
        GameState gs = GameState.getInstance();
        if (gs.isPvP()) return pickingForPlayer == 1 ? "BLUE TEAM" : "RED TEAM";
        if (gs.isArcade()) return "ARCADE MODE";
        return "PLAYER";
    }

    private Color getBadgeColor() {
        if (GameState.getInstance().isPvP() && pickingForPlayer == 2)
            return Theme.RED_TEAM;
        if (GameState.getInstance().isArcade()) return Theme.GOLD;
        return Theme.BLUE_TEAM;
    }

    private JLabel miniStat(String text, Color color) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Monospaced", Font.BOLD, 10));
        lbl.setForeground(color);
        return lbl;
    }

    private String getInitials(String name) {
        StringBuilder sb = new StringBuilder();
        for (String p : name.split(" ")) if (!p.isEmpty()) sb.append(p.charAt(0));
        return sb.toString().toUpperCase();
    }
}
