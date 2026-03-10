package game;

import gui.MainMenu;

import javax.swing.*;

/**
 * ╔══════════════════════════════════════════════════════════════╗
 * ║           AVENGERS DISASSEMBLE – Entry Point                 ║
 * ║   Turn-Based Strategy Game | Java Swing | JDK 17            ║
 * ╚══════════════════════════════════════════════════════════════╝
 *
 * Starts the Swing event-dispatch thread and launches the Main Menu.
 */
public class Main {

    public static void main(String[] args) {
        // ── Set system look and feel for better cross-platform appearance ──
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // ── Apply global UI defaults (dark theme) ──
        applyGlobalDefaults();

        // ── Launch on the EDT ──
        SwingUtilities.invokeLater(() -> {
            LoginManager  loginManager = new LoginManager();
            Leaderboard   leaderboard  = new Leaderboard();

            MainMenu mainMenu = new MainMenu(loginManager, leaderboard);
            mainMenu.setVisible(true);
        });
    }

    private static void applyGlobalDefaults() {
        UIManager.put("Panel.background",           new java.awt.Color(10, 10, 30));
        UIManager.put("OptionPane.background",      new java.awt.Color(10, 10, 30));
        UIManager.put("OptionPane.messageForeground",new java.awt.Color(220, 220, 255));
        UIManager.put("Button.background",          new java.awt.Color(40, 40, 80));
        UIManager.put("Button.foreground",          new java.awt.Color(220, 220, 255));
        UIManager.put("ScrollPane.background",      new java.awt.Color(5, 5, 20));
        UIManager.put("TextArea.background",        new java.awt.Color(5, 5, 20));
        UIManager.put("TextArea.foreground",        new java.awt.Color(220, 220, 255));
        UIManager.put("TextField.background",       new java.awt.Color(20, 20, 50));
        UIManager.put("TextField.foreground",       new java.awt.Color(220, 220, 255));
        UIManager.put("Label.foreground",           new java.awt.Color(220, 220, 255));
    }
}
