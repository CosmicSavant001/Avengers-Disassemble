package gui;

import java.awt.*;

/**
 * Cosmic Marvel colour palette – used by all GUI screens.
 * Change values here to update the entire game's look at once.
 */
public class Theme {

    // ── Backgrounds ───────────────────────────────────────────────────────
    public static final Color BG_DARK      = new Color(5,   5,  26);   // #05051A space black
    public static final Color BG_PRIMARY   = new Color(26,  26, 78);   // #1A1A4E deep cosmic blue
    public static final Color BG_CARD      = new Color(10,  10, 40);   // card background
    public static final Color BG_PANEL     = new Color(8,   8,  30);   // panel background

    // ── Accents ───────────────────────────────────────────────────────────
    public static final Color GOLD         = new Color(255, 215,  0);  // #FFD700 star gold
    public static final Color NEBULA_PINK  = new Color(255, 107,157);  // #FF6B9D nebula pink
    public static final Color COSMIC_BLUE  = new Color(79,  195,247);  // #4FC3F7 electric blue

    // ── Text ──────────────────────────────────────────────────────────────
    public static final Color TEXT_PRIMARY = new Color(232, 232,255);  // #E8E8FF starlight white
    public static final Color TEXT_DIM     = new Color(150, 150,190);  // dimmed text

    // ── Teams ─────────────────────────────────────────────────────────────
    public static final Color BLUE_TEAM    = new Color(50,  130,255);  // blue team
    public static final Color RED_TEAM     = new Color(255,  60, 60);  // red team / enemy

    // ── HP Bars ───────────────────────────────────────────────────────────
    public static final Color HP_HERO      = new Color(80,  220,120);  // hero HP green
    public static final Color HP_ENEMY     = new Color(255, 107,157);  // enemy HP pink

    // ── Buttons ───────────────────────────────────────────────────────────
    public static final Color BTN_DARK     = new Color(10,  10,  46);  // #0A0A2E deep space
    public static final Color BTN_HOVER    = new Color(30,  30,  90);

    // ── Fonts ─────────────────────────────────────────────────────────────
    public static final Font FONT_TITLE    = new Font("Impact", Font.BOLD,  56);
    public static final Font FONT_HEADING  = new Font("Impact", Font.PLAIN, 28);
    public static final Font FONT_BODY     = new Font("Arial",  Font.BOLD,  13);
    public static final Font FONT_SMALL    = new Font("Arial",  Font.PLAIN, 11);
    public static final Font FONT_MONO     = new Font("Monospaced", Font.PLAIN, 12);
}
