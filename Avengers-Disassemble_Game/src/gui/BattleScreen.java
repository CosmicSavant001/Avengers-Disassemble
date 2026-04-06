package gui;

import battle.BattleManager;
import battle.BattleResult;
import characters.*;
import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleScreen extends JFrame {

    // ─── References ──────────────────────────────────────────────────────
    private final JFrame        parentFrame;
    private final Leaderboard   leaderboard;
    private final Hero          hero;
    private       Hero          currentEnemy;
    private       BattleManager battleManager;
    private       int           totalScore  = 0;
    private       List<Hero>    arcadeWaves = new ArrayList<>();
    private       int           currentWave = 0;

    // ─── Colours / Fonts ─────────────────────────────────────────────────
    private static final Color BG_COLOR       = new Color(10, 10, 30);
    private static final Color ACCENT         = new Color(200, 30, 30);
    private static final Color TEXT_COLOR     = new Color(220, 220, 255);
    private static final Color HERO_HP_COLOR  = new Color(50, 200, 50);
    private static final Color ENEMY_HP_COLOR = new Color(200, 50, 50);
    private static final Color LOG_BG         = new Color(5, 5, 20);
    private static final Font  MAIN_FONT      = new Font("Arial", Font.BOLD, 13);

    // ─── Widgets ─────────────────────────────────────────────────────────
    private JProgressBar heroHPBar;
    private JProgressBar enemyHPBar;
    private JLabel       heroHPLabel;
    private JLabel       enemyHPLabel;
    private JLabel       heroNameLabel;
    private JLabel       enemyNameLabel;
    private JPanel       heroPortrait;
    private JPanel       enemyPortrait;
    private JTextArea    battleLogArea;
    private JButton      attackBtn;
    private JButton      skill1Btn;
    private JButton      skill2Btn;
    private JButton      skill3Btn;
    private JButton      defendBtn;
    private JLabel       turnLabel;
    private JLabel       scoreLabel;

    // ─── Constructor ─────────────────────────────────────────────────────

    public BattleScreen(JFrame parent, Leaderboard leaderboard) {
        this.parentFrame = parent;
        this.leaderboard = leaderboard;
        this.hero        = GameState.getInstance().getSelectedHero();

        setTitle("Avengers Disassemble – Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        if (GameState.getInstance().isArcade()) {
            arcadeWaves = generateArcadeWaves();
            startNewBattle(arcadeWaves.get(currentWave));
        } else if (GameState.getInstance().isPvE()) {
            startNewBattle(getRandomHero());
        } else {
            // PvP
            startNewBattle(GameState.getInstance().getSelectedHero2());
        }

        buildUI();
        updateUI();
        AudioManager.getInstance().playMusic(AudioManager.MUSIC_BATTLE);
    }

    // ─── Battle Setup ─────────────────────────────────────────────────────

    private void startNewBattle(Hero enemy) {
        this.currentEnemy  = enemy;
        this.battleManager = new BattleManager(hero, enemy);
    }

    /** All 10 characters shuffled, player's own character removed */
    private List<Hero> generateArcadeWaves() {
        List<Hero> roster = buildFullRoster();
        Collections.shuffle(roster);
        roster.removeIf(h -> h.getName().equals(hero.getName()));
        // Ensure exactly 10 waves — pad with Ultron if player picked Ultron
        while (roster.size() < 10) roster.add(new Ultron());
        return roster.subList(0, 10);
    }

    /** Random single opponent for PvE, not the same as player */
    private Hero getRandomHero() {
        List<Hero> roster = buildFullRoster();
        roster.removeIf(h -> h.getName().equals(hero.getName()));
        return roster.get((int)(Math.random() * roster.size()));
    }

    private List<Hero> buildFullRoster() {
        return new ArrayList<>(List.of(
                new Thor(),
                new IronMan(),
                new BlackWidow(),
                new Hulk(),
                new SpiderMan(),
                new ScarletWitch(),
                new DoctorStrange(),
                new Cadie(),
                new Loki(),
                new Ultron()
        ));
    }

    // ─── UI Construction ──────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(BG_COLOR);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        root.add(buildTopBar(),      BorderLayout.NORTH);
        root.add(buildArenaPanel(),  BorderLayout.CENTER);
        root.add(buildBottomPanel(), BorderLayout.SOUTH);

        add(root);
    }

    // ── Top bar ────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(20, 5, 5));
        bar.setBorder(new EmptyBorder(4, 10, 4, 10));

        JLabel gameTitle = new JLabel("⚡ AVENGERS DISASSEMBLE");
        gameTitle.setForeground(ACCENT);
        gameTitle.setFont(new Font("Impact", Font.PLAIN, 18));

        turnLabel = new JLabel("Turn 1", SwingConstants.CENTER);
        turnLabel.setForeground(TEXT_COLOR);
        turnLabel.setFont(MAIN_FONT);

        scoreLabel = new JLabel("Score: 0", SwingConstants.RIGHT);
        scoreLabel.setForeground(new Color(255, 215, 0));
        scoreLabel.setFont(MAIN_FONT);

        bar.add(gameTitle,  BorderLayout.WEST);
        bar.add(turnLabel,  BorderLayout.CENTER);
        bar.add(scoreLabel, BorderLayout.EAST);
        return bar;
    }

    // ── Arena ──────────────────────────────────────────────────────────
    private JPanel buildArenaPanel() {
        JPanel arena = new JPanel(new GridLayout(1, 3, 12, 0));
        arena.setBackground(BG_COLOR);
        arena.add(buildCharacterPanel(true));
        arena.add(buildBattleLogPanel());
        arena.add(buildCharacterPanel(false));
        return arena;
    }

    private JPanel buildCharacterPanel(boolean isHero) {
        JPanel panel = new JPanel(new BorderLayout(4, 8));
        panel.setBackground(new Color(15, 15, 40));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                new EmptyBorder(10, 10, 10, 10)));

        JLabel nameLabel = new JLabel(
                isHero ? hero.getName() : currentEnemy.getName(),
                SwingConstants.CENTER);
        nameLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        nameLabel.setForeground(isHero
                ? new Color(100, 180, 255) : new Color(255, 100, 100));
        if (isHero) heroNameLabel = nameLabel;
        else        enemyNameLabel = nameLabel;
        panel.add(nameLabel, BorderLayout.NORTH);

        JPanel portrait = buildPortrait(isHero);
        if (isHero) heroPortrait = portrait;
        else        enemyPortrait = portrait;
        panel.add(portrait, BorderLayout.CENTER);

        JPanel hpGroup = new JPanel(new BorderLayout(4, 2));
        hpGroup.setBackground(new Color(15, 15, 40));

        JProgressBar hpBar = new JProgressBar(0,
                isHero ? hero.getMaxHealth() : currentEnemy.getMaxHealth());
        hpBar.setValue(isHero
                ? hero.getCurrentHealth() : currentEnemy.getCurrentHealth());
        hpBar.setForeground(isHero ? HERO_HP_COLOR : ENEMY_HP_COLOR);
        hpBar.setBackground(new Color(30, 30, 30));
        hpBar.setStringPainted(false);
        hpBar.setPreferredSize(new Dimension(0, 18));
        if (isHero) heroHPBar = hpBar; else enemyHPBar = hpBar;

        JLabel hpLabel = new JLabel(
                isHero
                        ? hero.getCurrentHealth() + " / " + hero.getMaxHealth()
                        : currentEnemy.getCurrentHealth() + " / " + currentEnemy.getMaxHealth(),
                SwingConstants.CENTER);
        hpLabel.setForeground(TEXT_COLOR);
        hpLabel.setFont(new Font("Monospaced", Font.BOLD, 12));
        if (isHero) heroHPLabel = hpLabel; else enemyHPLabel = hpLabel;

        JLabel hpTitle = new JLabel("HP", SwingConstants.CENTER);
        hpTitle.setForeground(TEXT_COLOR);
        hpTitle.setFont(MAIN_FONT);
        hpGroup.add(hpTitle,  BorderLayout.NORTH);
        hpGroup.add(hpBar,    BorderLayout.CENTER);
        hpGroup.add(hpLabel,  BorderLayout.SOUTH);
        panel.add(hpGroup, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPortrait(boolean isHero) {
        Color  baseColor = isHero ? new Color(30, 60, 120) : new Color(80, 20, 20);
        String imgPath   = isHero ? hero.getImagePath()    : currentEnemy.getImagePath();
        String label     = isHero ? hero.getName()         : currentEnemy.getName();

        java.awt.image.BufferedImage loaded = null;
        try {
            java.io.File f = new java.io.File(imgPath);
            if (f.exists()) loaded = javax.imageio.ImageIO.read(f);
        } catch (Exception ignored) {}
        final java.awt.image.BufferedImage portrait = loaded;

        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                if (portrait != null) {
                    g2.setColor(baseColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                    g2.drawImage(portrait, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g2.setColor(baseColor);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(baseColor.brighter());
                    int cx = getWidth() / 2;
                    g2.fillOval(cx - 28, 20, 56, 56);
                    g2.fillRoundRect(cx - 36, 74, 72, 90, 8, 8);
                    g2.setColor(new Color(255, 255, 255, 200));
                    g2.setFont(new Font("Impact", Font.PLAIN, 12));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(label,
                            (getWidth() - fm.stringWidth(label)) / 2,
                            getHeight() - 10);
                }
            }
        };
        p.setPreferredSize(new Dimension(160, 200));
        return p;
    }

    private JPanel buildBattleLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT), " Battle Log ");
        tb.setTitleColor(ACCENT);
        tb.setTitleFont(MAIN_FONT);
        panel.setBorder(tb);

        battleLogArea = new JTextArea();
        battleLogArea.setEditable(false);
        battleLogArea.setLineWrap(true);
        battleLogArea.setWrapStyleWord(true);
        battleLogArea.setBackground(LOG_BG);
        battleLogArea.setForeground(TEXT_COLOR);
        battleLogArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        battleLogArea.setBorder(new EmptyBorder(6, 6, 6, 6));

        JScrollPane scroll = new JScrollPane(battleLogArea);
        scroll.setBackground(LOG_BG);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── Bottom panel: skill buttons ────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        String s1 = hero.getSkill1() != null ? hero.getSkill1().getName() : "Skill 1";
        String s2 = hero.getSkill2() != null ? hero.getSkill2().getName() : "Skill 2";
        String s3 = hero.getSkill3() != null ? hero.getSkill3().getName() : "Skill 3";

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(BG_COLOR);

        skill1Btn = actionButton("⚡ " + s1,
                new Color(30, 100, 180),
                e -> performAction(BattleManager.PlayerAction.SKILL_1));
        skill2Btn = actionButton("🌀 " + s2,
                new Color(100, 30, 180),
                e -> performAction(BattleManager.PlayerAction.SKILL_2));
        skill3Btn = actionButton("💥 " + s3,
                new Color(180, 100, 0),
                e -> performAction(BattleManager.PlayerAction.SKILL_3));

        btnPanel.add(skill1Btn);
        btnPanel.add(skill2Btn);
        btnPanel.add(skill3Btn);

        panel.add(btnPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton actionButton(String text, Color bg, ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.brighter(), 2),
                new EmptyBorder(8, 14, 8, 14)));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(l);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bg); }
        });
        return btn;
    }

    // ─── Battle Logic ─────────────────────────────────────────────────────

    private void performAction(BattleManager.PlayerAction action) {
        if (battleManager.isBattleOver()) return;

        switch (action) {
            case ATTACK  -> AudioManager.getInstance().playSFX(AudioManager.SFX_ATTACK);
            case SKILL_1 -> { if (hero.getSkill1() != null)
                AudioManager.getInstance().playSFX(hero.getSkill1().getSoundFile()); }
            case SKILL_2 -> { if (hero.getSkill2() != null)
                AudioManager.getInstance().playSFX(hero.getSkill2().getSoundFile()); }
            case SKILL_3 -> { if (hero.getSkill3() != null)
                AudioManager.getInstance().playSFX(hero.getSkill3().getSoundFile()); }
            case DEFEND  -> AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        }

        BattleResult result = battleManager.executeTurn(action);
        appendLog(result.getFullLog());
        updateUI();

        if (result.isBattleOver()) handleBattleEnd(result);
    }

    private void handleBattleEnd(BattleResult result) {
        setButtonsEnabled(false);

        if (result.isPlayerWon()) {
            totalScore += 200;
            GameState.getInstance().recordVictory(200);
            AudioManager.getInstance().playSFX(AudioManager.SFX_VICTORY);
            scoreLabel.setText("Score: " + totalScore);
            Timer timer = new Timer(1500, e -> promptNextBattle());
            timer.setRepeats(false);
            timer.start();
        } else {
            GameState.getInstance().recordDefeat();
            AudioManager.getInstance().playSFX(AudioManager.SFX_DEFEAT);
            Timer timer = new Timer(1500, e -> openGameOver(false));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void promptNextBattle() {
        if (GameState.getInstance().isArcade()) {
            currentWave++;
            if (currentWave >= arcadeWaves.size()) {
                appendLog("\n🏆 YOU CLEARED ALL 10 WAVES! ARCADE COMPLETE!\n");
                openGameOver(true);
                return;
            }
            Hero next = arcadeWaves.get(currentWave);
            startNewBattle(next);
            resetUIForNewBattle();
            appendLog("\n── Wave " + (currentWave + 1) + " / 10 : "
                    + next.getName() + " ──\n");
            setButtonsEnabled(true);

        } else {
            // PvE and PvP — single fight, go straight to game over
            openGameOver(true);
        }
    }

    private void resetUIForNewBattle() {
        enemyNameLabel.setText(currentEnemy.getName());
        enemyHPBar.setMaximum(currentEnemy.getMaxHealth());
        updateUI();
        setButtonsEnabled(true);
        heroPortrait.repaint();
        enemyPortrait.repaint();
    }

    // ─── UI Update ────────────────────────────────────────────────────────

    private void updateUI() {
        heroHPBar.setMaximum(hero.getMaxHealth());
        heroHPBar.setValue(hero.getCurrentHealth());
        heroHPLabel.setText(hero.getCurrentHealth() + " / " + hero.getMaxHealth());

        enemyHPBar.setMaximum(currentEnemy.getMaxHealth());
        enemyHPBar.setValue(currentEnemy.getCurrentHealth());
        enemyHPLabel.setText(currentEnemy.getCurrentHealth()
                + " / " + currentEnemy.getMaxHealth());

        // Turn / wave label
        if (GameState.getInstance().isArcade()) {
            turnLabel.setText("Wave " + (currentWave + 1) + "/10  |  Turn "
                    + battleManager.getTurnNumber());
        } else {
            turnLabel.setText("Turn " + battleManager.getTurnNumber());
        }

        scoreLabel.setText("Score: " + totalScore);

        // Update skill button cooldown status
        updateSkillButton(skill1Btn, hero.getSkill1());
        updateSkillButton(skill2Btn, hero.getSkill2());
        updateSkillButton(skill3Btn, hero.getSkill3());

        revalidate();
        repaint();
    }

    private void updateSkillButton(JButton btn, abilities.Ability skill) {
        if (skill == null) { btn.setEnabled(false); return; }
        btn.setEnabled(true);
    }

    private void appendLog(String text) {
        battleLogArea.append(text + "\n");
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }

    private void setButtonsEnabled(boolean enabled) {
        skill1Btn.setEnabled(enabled);
        skill2Btn.setEnabled(enabled);
        skill3Btn.setEnabled(enabled);
    }

    // ─── Navigation ───────────────────────────────────────────────────────

    private void openGameOver(boolean won) {
        SwingUtilities.invokeLater(() -> {
            GameOverScreen gos = new GameOverScreen(
                    parentFrame, leaderboard,
                    hero.getName(), totalScore, won);
            gos.setVisible(true);
            dispose();
        });
    }
}
