package gui;

import battle.BattleManager;
import battle.BattleResult;
import characters.Enemy;
import characters.Hero;
import game.AudioManager;
import game.GameState;
import game.Leaderboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * BattleScreen – the main combat interface.
 * Displays hero and enemy portraits, health bars, the battle log,
 * and action buttons (Attack / Ability / Defend).
 */
public class BattleScreen extends JFrame {

    // ─── References ──────────────────────────────────────────────────────
    private final JFrame      parentFrame;
    private final Leaderboard leaderboard;
    private final Hero        hero;
    private       Enemy       currentEnemy;
    private       BattleManager battleManager;
    private int               totalScore = 0;

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
    private JButton      abilityBtn;
    private JButton      defendBtn;
    private JLabel       turnLabel;
    private JLabel       scoreLabel;
    private JLabel       abilityStatusLabel;

    public BattleScreen(JFrame parent, Leaderboard leaderboard) {
        this.parentFrame = parent;
        this.leaderboard = leaderboard;
        this.hero        = GameState.getInstance().getSelectedHero();

        setTitle("Avengers Disassemble – Battle");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        // Choose opponent based on game mode
        if (GameState.getInstance().isPvAI()) {
            startNewBattle(Enemy.createHydraAgent());
        } else {
            // PvP or AIvAI: opponent is hero2 wrapped as a pseudo-enemy
            startNewBattle(Enemy.createHydraAgent()); // placeholder, see BattleManager extension
        }

        buildUI();
        updateUI();
        AudioManager.getInstance().playMusic(AudioManager.MUSIC_BATTLE);

        // AI vs AI: auto-run turns with a timer
        if (GameState.getInstance().isAIvAI()) {
            setButtonsEnabled(false);
            appendLog("[ AI vs AI Mode - watch the battle! ]\n");
            runAIvsAI();
        }
    }

    private void startNewBattle(Enemy enemy) {
        this.currentEnemy   = enemy;
        this.battleManager  = new BattleManager(hero, enemy);
    }

    // ─── UI Construction ─────────────────────────────────────────────────

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBackground(BG_COLOR);
        root.setBorder(new EmptyBorder(12, 12, 12, 12));

        root.add(buildTopBar(),      BorderLayout.NORTH);
        root.add(buildArenaPanel(),  BorderLayout.CENTER);
        root.add(buildBottomPanel(), BorderLayout.SOUTH);

        add(root);
    }

    // ── Top bar: score + turn counter ──────────────────────────────────
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

    // ── Main arena: hero | log | enemy ─────────────────────────────────
    private JPanel buildArenaPanel() {
        JPanel arena = new JPanel(new GridLayout(1, 3, 12, 0));
        arena.setBackground(BG_COLOR);

        arena.add(buildCharacterPanel(true));   // Hero (left)
        arena.add(buildBattleLogPanel());        // Log (center)
        arena.add(buildCharacterPanel(false));  // Enemy (right)

        return arena;
    }

    private JPanel buildCharacterPanel(boolean isHero) {
        JPanel panel = new JPanel(new BorderLayout(4, 8));
        panel.setBackground(new Color(15, 15, 40));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Name
        JLabel nameLabel = new JLabel(isHero ? hero.getName() : currentEnemy.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Impact", Font.PLAIN, 20));
        nameLabel.setForeground(isHero ? new Color(100, 180, 255) : new Color(255, 100, 100));
        if (isHero) heroNameLabel = nameLabel; else enemyNameLabel = nameLabel;
        panel.add(nameLabel, BorderLayout.NORTH);

        // Portrait placeholder
        JPanel portrait = buildPortrait(isHero);
        if (isHero) heroPortrait = portrait; else enemyPortrait = portrait;
        panel.add(portrait, BorderLayout.CENTER);

        // HP bar
        JPanel hpGroup = new JPanel(new BorderLayout(4, 2));
        hpGroup.setBackground(new Color(15, 15, 40));

        JProgressBar hpBar = new JProgressBar(0, isHero ? hero.getMaxHealth() : currentEnemy.getMaxHealth());
        hpBar.setValue(isHero ? hero.getCurrentHealth() : currentEnemy.getCurrentHealth());
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
        hpGroup.add(hpTitle, BorderLayout.NORTH);
        hpGroup.add(hpBar,    BorderLayout.CENTER);
        hpGroup.add(hpLabel,  BorderLayout.SOUTH);

        panel.add(hpGroup, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPortrait(boolean isHero) {
        Color baseColor = isHero ? new Color(30, 60, 120) : new Color(80, 20, 20);
        String imgPath  = isHero ? hero.getImagePath() : currentEnemy.getImagePath();
        String label    = isHero ? hero.getName()      : currentEnemy.getName();

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
                    g2.drawString(label, (getWidth() - fm.stringWidth(label)) / 2, getHeight() - 10);
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

    // ── Bottom panel: ability info + action buttons ─────────────────────
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Ability status
        String abilityName = (hero.getSpecialAbility() != null)
                ? hero.getSpecialAbility().getName() : "None";
        abilityStatusLabel = new JLabel("⚡ " + abilityName + " – READY");
        abilityStatusLabel.setForeground(new Color(100, 220, 255));
        abilityStatusLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        // Action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(BG_COLOR);

        attackBtn  = actionButton("⚔ ATTACK",  new Color(180, 30, 30), e -> performAction(BattleManager.PlayerAction.ATTACK));
        abilityBtn = actionButton("⚡ ABILITY", new Color(30, 100, 180), e -> performAction(BattleManager.PlayerAction.USE_ABILITY));
        defendBtn  = actionButton("🛡 DEFEND",  new Color(30, 120, 30), e -> performAction(BattleManager.PlayerAction.DEFEND));

        btnPanel.add(attackBtn);
        btnPanel.add(abilityBtn);
        btnPanel.add(defendBtn);

        panel.add(abilityStatusLabel, BorderLayout.WEST);
        panel.add(btnPanel,           BorderLayout.EAST);
        return panel;
    }

    private JButton actionButton(String text, Color bg, ActionListener l) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.brighter(), 2),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(l);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    // ─── Battle Logic ──────────────────────────────────────────────────

    private void performAction(BattleManager.PlayerAction action) {
        if (battleManager.isBattleOver()) return;

        // Play sound
        switch (action) {
            case ATTACK     -> AudioManager.getInstance().playSFX(AudioManager.SFX_ATTACK);
            case USE_ABILITY-> {
                if (hero.getSpecialAbility() != null)
                    AudioManager.getInstance().playSFX(hero.getSpecialAbility().getSoundFile());
            }
            case DEFEND     -> AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        }

        // Execute the turn
        BattleResult result = battleManager.executeTurn(action);
        appendLog(result.getFullLog());
        updateUI();

        if (result.isBattleOver()) {
            handleBattleEnd(result);
        }
    }

    private void handleBattleEnd(BattleResult result) {
        setButtonsEnabled(false);

        if (result.isPlayerWon()) {
            // Award score
            totalScore += currentEnemy.getScoreReward();
            GameState.getInstance().recordVictory(currentEnemy.getScoreReward());
            AudioManager.getInstance().playSFX(AudioManager.SFX_VICTORY);
            scoreLabel.setText("Score: " + totalScore);

            // After short delay, ask to fight next enemy
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
        String[] options = {"Fight Next Enemy", "Retreat (Main Menu)"};
        int choice = JOptionPane.showOptionDialog(this,
                "Enemy defeated!\nScore: " + totalScore + "\nContinue battling?",
                "Victory!",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            Enemy next = selectNextEnemy();
            startNewBattle(next);
            resetUIForNewBattle();
            appendLog("\n── New Battle: " + next.getName() + " ──\n");
        } else {
            openGameOver(true);
        }
    }

    private Enemy selectNextEnemy() {
        int wins = GameState.getInstance().getBattlesWon();
        if (wins >= 3) return Enemy.createThanos();
        if (wins >= 2) return Enemy.createUltron();
        if (wins >= 1) return Enemy.createLoki();
        return Enemy.createHydraAgent();
    }

    private void resetUIForNewBattle() {
        enemyNameLabel.setText(currentEnemy.getName());
        enemyHPBar.setMaximum(currentEnemy.getMaxHealth());
        updateUI();
        setButtonsEnabled(true);
        heroPortrait.repaint();
        enemyPortrait.repaint();
    }

    // ─── UI Update ────────────────────────────────────────────────────

    private void updateUI() {
        // Hero HP
        heroHPBar.setMaximum(hero.getMaxHealth());
        heroHPBar.setValue(hero.getCurrentHealth());
        heroHPLabel.setText(hero.getCurrentHealth() + " / " + hero.getMaxHealth());

        // Enemy HP
        enemyHPBar.setMaximum(currentEnemy.getMaxHealth());
        enemyHPBar.setValue(currentEnemy.getCurrentHealth());
        enemyHPLabel.setText(currentEnemy.getCurrentHealth() + " / " + currentEnemy.getMaxHealth());

        // Turn label
        turnLabel.setText("Turn " + battleManager.getTurnNumber());

        // Score
        scoreLabel.setText("Score: " + totalScore);

        // Ability status
        if (hero.getSpecialAbility() != null) {
            String status = hero.getSpecialAbility().isReady()
                    ? "READY"
                    : "Cooldown: " + hero.getSpecialAbility().getCooldownRemaining();
            abilityStatusLabel.setText("⚡ " + hero.getSpecialAbility().getName() + " – " + status);
            abilityStatusLabel.setForeground(hero.getSpecialAbility().isReady()
                    ? new Color(100, 220, 255) : new Color(180, 100, 100));
            abilityBtn.setEnabled(hero.getSpecialAbility().isReady());
        }

        revalidate();
        repaint();
    }

    private void appendLog(String text) {
        battleLogArea.append(text + "\n");
        // Auto-scroll to bottom
        battleLogArea.setCaretPosition(battleLogArea.getDocument().getLength());
    }

    private void setButtonsEnabled(boolean enabled) {
        attackBtn.setEnabled(enabled);
        abilityBtn.setEnabled(enabled);
        defendBtn.setEnabled(enabled);
    }

    // ─── Navigation ───────────────────────────────────────────────────

    private void openGameOver(boolean won) {
        SwingUtilities.invokeLater(() -> {
            GameOverScreen gos = new GameOverScreen(
                    parentFrame, leaderboard,
                    hero.getName(), totalScore, won);
            gos.setVisible(true);
            dispose();
        });
    }

    /** Runs AI vs AI by auto-firing turns every 1.2 seconds. */
    private void runAIvsAI() {
        Timer autoTimer = new Timer(1200, null);
        autoTimer.addActionListener(e -> {
            if (battleManager.isBattleOver()) {
                autoTimer.stop();
                return;
            }
            // Both sides use AI — player side also picks randomly
            BattleManager.PlayerAction[] actions = BattleManager.PlayerAction.values();
            BattleManager.PlayerAction randomAction =
                    actions[(int)(Math.random() * actions.length)];
            BattleResult result = battleManager.executeTurn(randomAction);
            appendLog(result.getFullLog());
            updateUI();
            if (result.isBattleOver()) {
                autoTimer.stop();
                handleBattleEnd(result);
            }
        });
        autoTimer.start();
    }
}

