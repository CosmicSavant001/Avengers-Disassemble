package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages audio playback for the game.
 * Sound files are .wav clips loaded from the assets/audio/ directory.
 * Missing files are silently ignored so the game runs without audio assets.
 */
public class AudioManager {

    private static AudioManager instance;

    // ─── Pre-defined sound keys ────────────────────────────────────────────
    public static final String SFX_ATTACK         = "assets/audio/attack.wav";
    public static final String SFX_REPULSOR        = "assets/audio/repulsor_blast.wav";
    public static final String SFX_SHIELD_THROW    = "assets/audio/shield_throw.wav";
    public static final String SFX_LIGHTNING       = "assets/audio/lightning_strike.wav";
    public static final String SFX_SMASH           = "assets/audio/smash.wav";
    public static final String SFX_WIDOW_STING     = "assets/audio/widows_sting.wav";
    public static final String SFX_VICTORY         = "assets/audio/victory.wav";
    public static final String SFX_DEFEAT          = "assets/audio/defeat.wav";
    public static final String SFX_BUTTON_CLICK    = "assets/audio/button_click.wav";
    public static final String MUSIC_MENU          = "assets/audio/menu_music.wav";
    public static final String MUSIC_BATTLE        = "assets/audio/battle_music.wav";

    private Map<String, Clip> clipCache = new HashMap<>();
    private Clip currentMusic = null;
    private boolean sfxEnabled   = true;
    private boolean musicEnabled = true;

    // ─── Singleton ──────────────────────────────────────────────────────────
    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    // ─── Public API ──────────────────────────────────────────────────────────

    /** Plays a short sound effect (non-blocking). */
    public void playSFX(String filePath) {
        if (!sfxEnabled) return;
        Clip clip = loadClip(filePath);
        if (clip == null) return;
        if (clip.isRunning()) clip.stop();
        clip.setFramePosition(0);
        clip.start();
    }

    /** Starts looping background music. Previous music is stopped. */
    public void playMusic(String filePath) {
        if (!musicEnabled) return;
        stopMusic();
        Clip clip = loadClip(filePath);
        if (clip == null) return;
        currentMusic = clip;
        currentMusic.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** Stops the currently playing background music. */
    public void stopMusic() {
        if (currentMusic != null && currentMusic.isRunning()) {
            currentMusic.stop();
        }
        currentMusic = null;
    }

    public void setSFXEnabled(boolean enabled)   { this.sfxEnabled   = enabled; }
    public void setMusicEnabled(boolean enabled) { this.musicEnabled = enabled; if (!enabled) stopMusic(); }
    public boolean isSFXEnabled()   { return sfxEnabled; }
    public boolean isMusicEnabled() { return musicEnabled; }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    private Clip loadClip(String filePath) {
        if (clipCache.containsKey(filePath)) return clipCache.get(filePath);

        File f = new File(filePath);
        if (!f.exists()) {
            // Silently ignore missing audio assets
            clipCache.put(filePath, null);
            return null;
        }
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clipCache.put(filePath, clip);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Audio error for " + filePath + ": " + e.getMessage());
            clipCache.put(filePath, null);
            return null;
        }
    }
}
