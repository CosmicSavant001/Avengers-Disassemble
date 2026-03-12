package game;

import characters.Hero;

/**
 * Singleton holding the global game state.
 * Shared across all screens and managers via getInstance().
 */
public class GameState {

    // ── Game Modes ─────────────────────────────────────────────────────────
    public enum GameMode {
        PLAYER_VS_AI,
        PLAYER_VS_PLAYER,
        AI_VS_AI
    }

    private static GameState instance;

    // ── Session Data ───────────────────────────────────────────────────────
    private GameMode selectedMode  = GameMode.PLAYER_VS_AI;
    private Hero     selectedHero  = null;
    private Hero     selectedHero2 = null; // for PvP / AI vs AI
    private int      currentScore  = 0;
    private int      battlesWon    = 0;
    private int      battlesLost   = 0;
    private int      enemiesDefeated = 0;

    private GameState() {}

    public static GameState getInstance() {
        if (instance == null) instance = new GameState();
        return instance;
    }

    // ── Score ──────────────────────────────────────────────────────────────

    public void addScore(int points)           { currentScore += points; }

    public void recordVictory(int scoreReward) {
        battlesWon++;
        enemiesDefeated++;
        addScore(scoreReward);
    }

    public void recordDefeat() { battlesLost++; }

    public void resetRunData() {
        currentScore    = 0;
        battlesWon      = 0;
        battlesLost     = 0;
        enemiesDefeated = 0;
        selectedHero    = null;
        selectedHero2   = null;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public GameMode getGameMode()                  { return selectedMode; }
    public void     setGameMode(GameMode mode)     { this.selectedMode  = mode; }

    public Hero  getSelectedHero()                 { return selectedHero; }
    public void  setSelectedHero(Hero hero)        { this.selectedHero  = hero; }

    public Hero  getSelectedHero2()                { return selectedHero2; }
    public void  setSelectedHero2(Hero hero)       { this.selectedHero2 = hero; }

    public int   getCurrentScore()                 { return currentScore; }
    public int   getBattlesWon()                   { return battlesWon; }
    public int   getBattlesLost()                  { return battlesLost; }
    public int   getEnemiesDefeated()              { return enemiesDefeated; }

    // Convenience: mode checks
    public boolean isPvP()   { return selectedMode == GameMode.PLAYER_VS_PLAYER; }
    public boolean isPvAI()  { return selectedMode == GameMode.PLAYER_VS_AI; }
    public boolean isAIvAI() { return selectedMode == GameMode.AI_VS_AI; }

    @Override
    public String toString() {
        return String.format("GameState[mode=%s, hero=%s, score=%d, W/L=%d/%d]",
                selectedMode,
                selectedHero != null ? selectedHero.getName() : "None",
                currentScore, battlesWon, battlesLost);
    }
}
