package game;

import gui.MainMenu;
import javax.swing.*;

/**
 * Entry point for Avengers Disassemble.
 * Launches the Main Menu with game mode selection.
 */
public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        applyGlobalDefaults();

        SwingUtilities.invokeLater(() -> {
            Leaderboard leaderboard = new Leaderboard();
            MainMenu mainMenu = new MainMenu(leaderboard);
            mainMenu.setVisible(true);
        });
    }

    private static void applyGlobalDefaults() {
        UIManager.put("Panel.background",            new java.awt.Color(10, 10, 30));
        UIManager.put("OptionPane.background",       new java.awt.Color(10, 10, 30));
        UIManager.put("OptionPane.messageForeground",new java.awt.Color(220, 220, 255));
        UIManager.put("Button.background",           new java.awt.Color(40, 40, 80));
        UIManager.put("Button.foreground",           new java.awt.Color(220, 220, 255));
        UIManager.put("ScrollPane.background",       new java.awt.Color(5, 5, 20));
        UIManager.put("TextArea.background",         new java.awt.Color(5, 5, 20));
        UIManager.put("TextArea.foreground",         new java.awt.Color(220, 220, 255));
        UIManager.put("Label.foreground",            new java.awt.Color(220, 220, 255));
    }
}
