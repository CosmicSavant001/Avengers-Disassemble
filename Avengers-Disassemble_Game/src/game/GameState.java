package game;

import characters.Hero;

/**
 * Singleton – holds all global game state.
 * Modes: PvE | PvP | Arcade
 */
public class GameState {

    public enum GameMode {
        PVE, PVP, ARCADE
    }

    private static GameState instance;

    // ── Session ───────────────────────────────────────────────────────────
    private GameMode selectedMode  = GameMode.PVE;
    private Hero     selectedHero  = null;
    private Hero     selectedHero2 = null;

    // ── Score & Stats ─────────────────────────────────────────────────────
    private int currentScore    = 0;
    private int battlesWon      = 0;
    private int battlesLost     = 0;
    private int enemiesDefeated = 0;

    // ── Arcade State ──────────────────────────────────────────────────────
    private int     arcadeRound   = 1;
    private int     arcadeScore   = 0;
    private boolean arcadeCleared = false;

    private GameState() {}

    public static GameState getInstance() {
        if (instance == null) instance = new GameState();
        return instance;
    }

    // ── Score ─────────────────────────────────────────────────────────────

    public void addScore(int pts)          { currentScore += pts; arcadeScore += pts; }
    public void recordVictory(int reward)  { battlesWon++; enemiesDefeated++; addScore(reward); }
    public void recordDefeat()             { battlesLost++; }

    // ── Arcade ────────────────────────────────────────────────────────────

    public void nextArcadeRound()         { arcadeRound++; }
    public void clearArcade()             { arcadeCleared = true; }
    public int  getArcadeRound()          { return arcadeRound; }
    public int  getArcadeScore()          { return arcadeScore; }
    public boolean isArcadeCleared()      { return arcadeCleared; }

    // ── Reset ─────────────────────────────────────────────────────────────

    public void resetRunData() {
        currentScore    = 0;
        battlesWon      = 0;
        battlesLost     = 0;
        enemiesDefeated = 0;
        selectedHero    = null;
        selectedHero2   = null;
        arcadeRound     = 1;
        arcadeScore     = 0;
        arcadeCleared   = false;
    }

    // ── Mode Checks ───────────────────────────────────────────────────────

    public boolean isPvE()    { return selectedMode == GameMode.PVE; }
    public boolean isPvP()    { return selectedMode == GameMode.PVP; }
    public boolean isArcade() { return selectedMode == GameMode.ARCADE; }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public GameMode getGameMode()           { return selectedMode; }
    public void     setGameMode(GameMode m) { this.selectedMode = m; }

    public Hero getSelectedHero()           { return selectedHero; }
    public void setSelectedHero(Hero h)     { this.selectedHero = h; }

    public Hero getSelectedHero2()          { return selectedHero2; }
    public void setSelectedHero2(Hero h)    { this.selectedHero2 = h; }

    public int  getCurrentScore()           { return currentScore; }
    public int  getBattlesWon()             { return battlesWon; }
    public int  getBattlesLost()            { return battlesLost; }
    public int  getEnemiesDefeated()        { return enemiesDefeated; }
}