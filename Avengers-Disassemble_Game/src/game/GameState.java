package game;

import characters.Hero;

/**
 * Singleton holding the global game state.
 * Shared across all screens and managers via getInstance().
 */
public class GameState {

    private static GameState instance;

    // ─── Session Data ──────────────────────────────────────────────────────
    private String loggedInUser  = "";
    private Hero   selectedHero  = null;
    private int    currentScore  = 0;
    private int    battlesWon    = 0;
    private int    battlesLost   = 0;
    private int    enemiesDefeated = 0;

    // ─── Singleton ──────────────────────────────────────────────────────────
    private GameState() {}

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    // ─── Score Management ──────────────────────────────────────────────────

    public void addScore(int points) {
        currentScore += points;
    }

    public void recordVictory(int scoreReward) {
        battlesWon++;
        enemiesDefeated++;
        addScore(scoreReward);
    }

    public void recordDefeat() {
        battlesLost++;
    }

    /** Resets in-battle progress (new run). */
    public void resetRunData() {
        currentScore     = 0;
        battlesWon       = 0;
        battlesLost      = 0;
        enemiesDefeated  = 0;
        selectedHero     = null;
    }

    // ─── Getters & Setters ─────────────────────────────────────────────────

    public String getLoggedInUser()               { return loggedInUser; }
    public void   setLoggedInUser(String user)    { this.loggedInUser = user; }

    public Hero   getSelectedHero()               { return selectedHero; }
    public void   setSelectedHero(Hero hero)      { this.selectedHero = hero; }

    public int    getCurrentScore()               { return currentScore; }
    public int    getBattlesWon()                 { return battlesWon; }
    public int    getBattlesLost()                { return battlesLost; }
    public int    getEnemiesDefeated()            { return enemiesDefeated; }

    @Override
    public String toString() {
        return String.format("GameState[user=%s, hero=%s, score=%d, W/L=%d/%d]",
                loggedInUser,
                selectedHero != null ? selectedHero.getName() : "None",
                currentScore, battlesWon, battlesLost);
    }
}
