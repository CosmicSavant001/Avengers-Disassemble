package gui;

import game.AudioManager;
import game.GameState;
import game.Leaderboard;
import game.LoginManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Main Menu screen – entry point of the game UI.
 * Handles login / registration and navigates to Character Select or Leaderboard.
 */
public class MainMenu extends JFrame {

    // ─── Shared managers (passed down to child screens) ───────────────────
    private final LoginManager  loginManager;
    private final Leaderboard   leaderboard;

    // ─── Colours / Fonts ─────────────────────────────────────────────────
    private static final Color  BG_COLOR      = new Color(10, 10, 30);
    private static final Color  ACCENT_COLOR  = new Color(200, 30, 30);
    private static final Color  TEXT_COLOR    = new Color(220, 220, 255);
    private static final Color  BUTTON_COLOR  = new Color(40, 40, 80);
    private static final Font   TITLE_FONT    = new Font("Impact", Font.BOLD, 48);
    private static final Font   SUBTITLE_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font   BUTTON_FONT   = new Font("Arial", Font.BOLD, 14);

    // ─── Login fields ─────────────────────────────────────────────────────
    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         statusLabel;

    // ─── Panels ───────────────────────────────────────────────────────────
    private JPanel loginPanel;
    private JPanel mainPanel;

    public MainMenu(LoginManager loginManager, Leaderboard leaderboard) {
        this.loginManager = loginManager;
        this.leaderboard  = leaderboard;

        setTitle("Avengers Disassemble");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        buildUI();
        AudioManager.getInstance().playMusic(AudioManager.MUSIC_MENU);
    }

    // ─── UI Construction ─────────────────────────────────────────────────

    private void buildUI() {
        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(800, 600));

        // Background gradient panel
        JPanel bg = new GradientPanel();
        bg.setBounds(0, 0, 800, 600);
        layered.add(bg, JLayeredPane.DEFAULT_LAYER);

        // Content panel on top
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBounds(0, 0, 800, 600);
        layered.add(content, JLayeredPane.PALETTE_LAYER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx  = 0;

        // ── Title ──
        JLabel title = new JLabel("AVENGERS", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(ACCENT_COLOR);
        gbc.gridy = 0;
        content.add(title, gbc);

        JLabel subtitle = new JLabel("D I S A S S E M B L E", SwingConstants.CENTER);
        subtitle.setFont(SUBTITLE_FONT);
        subtitle.setForeground(TEXT_COLOR);
        gbc.gridy = 1;
        content.add(subtitle, gbc);

        // ── Login Panel ──
        loginPanel = buildLoginPanel();
        gbc.gridy = 2;
        content.add(loginPanel, gbc);

        // ── Status label ──
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setForeground(new Color(255, 100, 100));
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        gbc.gridy = 3;
        content.add(statusLabel, gbc);

        add(layered);
        pack();
    }

    private JPanel buildLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(styledLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = styledTextField();
        panel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(styledLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(16);
        styleTextField(passwordField);
        panel.add(passwordField, gbc);

        // Buttons row
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(styledButton("LOGIN",    e -> handleLogin()));
        btnRow.add(styledButton("REGISTER", e -> handleRegister()));
        btnRow.add(styledButton("LEADERBOARD", e -> showLeaderboard()));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panel.add(btnRow, gbc);

        return panel;
    }

    // ─── Action Handlers ─────────────────────────────────────────────────

    private void handleLogin() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (loginManager.login(user, pass)) {
            GameState.getInstance().setLoggedInUser(user);
            setStatus("Welcome back, " + user + "!", new Color(100, 255, 100));
            openCharacterSelect();
        } else {
            setStatus("Invalid username or password.", new Color(255, 80, 80));
        }
    }

    private void handleRegister() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        String user = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            setStatus("Username and password cannot be empty.", new Color(255, 180, 0));
            return;
        }
        if (loginManager.register(user, pass)) {
            setStatus("Account created! You can now log in.", new Color(100, 255, 100));
        } else {
            setStatus("Username already taken. Choose another.", new Color(255, 80, 80));
        }
    }

    private void showLeaderboard() {
        AudioManager.getInstance().playSFX(AudioManager.SFX_BUTTON_CLICK);
        List<Leaderboard.Entry> scores = leaderboard.getTopScores();
        StringBuilder sb = new StringBuilder("═══════ LEADERBOARD ═══════\n\n");
        sb.append(String.format("%-4s %-15s %-12s %6s%n", "#", "Player", "Hero", "Score"));
        sb.append("─".repeat(42)).append("\n");
        int rank = 1;
        for (Leaderboard.Entry e : scores) {
            sb.append(String.format("%-4d %-15s %-12s %6d%n",
                    rank++, e.getUsername(), e.getHeroName(), e.getScore()));
        }
        if (scores.isEmpty()) sb.append("No scores recorded yet.\n");

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(BG_COLOR);
        ta.setForeground(TEXT_COLOR);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(420, 280));
        JOptionPane.showMessageDialog(this, sp, "Leaderboard", JOptionPane.PLAIN_MESSAGE);
    }

    private void openCharacterSelect() {
        SwingUtilities.invokeLater(() -> {
            CharacterSelect cs = new CharacterSelect(this, leaderboard);
            cs.setVisible(true);
            setVisible(false);
        });
    }

    // ─── Styling Helpers ─────────────────────────────────────────────────

    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_COLOR);
        lbl.setFont(BUTTON_FONT);
        return lbl;
    }

    private JTextField styledTextField() {
        JTextField tf = new JTextField(16);
        styleTextField(tf);
        return tf;
    }

    private void styleTextField(JTextField tf) {
        tf.setBackground(new Color(20, 20, 50));
        tf.setForeground(TEXT_COLOR);
        tf.setCaretColor(TEXT_COLOR);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                new EmptyBorder(4, 8, 4, 8)
        ));
        tf.setFont(new Font("Arial", Font.PLAIN, 13));
    }

    private JButton styledButton(String text, java.awt.event.ActionListener listener) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(TEXT_COLOR);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                new EmptyBorder(8, 16, 8, 16)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(listener);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(ACCENT_COLOR); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(BUTTON_COLOR); }
        });
        return btn;
    }

    private void setStatus(String msg, Color color) {
        statusLabel.setText(msg);
        statusLabel.setForeground(color);
    }

    // ─── Gradient Background ─────────────────────────────────────────────

    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(0, 0, BG_COLOR,
                    getWidth(), getHeight(), new Color(30, 0, 0));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // ─── Helper: simulate letter spacing (JLabel doesn't natively support it) ─
    // This is a no-op shim so we don't need to install custom font libraries.
    private void dummy() {} // intentionally empty
}

// Extension: add letter-spacing via custom renderer would go here in a real project.
// For now the SUBTITLE_FONT label handles it visually with the spaced-out string literal.
