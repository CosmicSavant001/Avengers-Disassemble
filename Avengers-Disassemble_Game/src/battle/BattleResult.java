package battle;

import java.util.ArrayList;
import java.util.List;

/**
 * Immutable (after construction) data object representing the outcome of one battle turn.
 * The GUI reads from this to update its display.
 */
public class BattleResult {

    private final int turnNumber;
    private String playerActionLog  = "";
    private String enemyActionLog   = "";
    private List<String> statusEffects = new ArrayList<>();
    private boolean battleOver  = false;
    private boolean playerWon   = false;
    private String  endMessage  = "";

    public BattleResult(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    // ─── Setters (package-private, set by BattleManager) ──────────────────

    void setPlayerActionLog(String log)  { this.playerActionLog = log; }
    void setEnemyActionLog(String log)   { this.enemyActionLog  = log; }
    void appendStatusEffect(String msg)  { this.statusEffects.add(msg); }
    void setBattleOver(boolean over)     { this.battleOver = over; }
    void setPlayerWon(boolean won)       { this.playerWon  = won; }
    void setEndMessage(String msg)       { this.endMessage = msg; }

    // ─── Getters ──────────────────────────────────────────────────────────

    public int    getTurnNumber()         { return turnNumber; }
    public String getPlayerActionLog()    { return playerActionLog; }
    public String getEnemyActionLog()     { return enemyActionLog; }
    public List<String> getStatusEffects(){ return new ArrayList<>(statusEffects); }
    public boolean isBattleOver()         { return battleOver; }
    public boolean isPlayerWon()          { return playerWon; }
    public String  getEndMessage()        { return endMessage; }

    /** Returns a single combined log string for easy display. */
    public String getFullLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Turn ").append(turnNumber).append("]\n");
        if (!playerActionLog.isEmpty()) sb.append("► ").append(playerActionLog).append("\n");
        for (String fx : statusEffects)  sb.append("✦ ").append(fx).append("\n");
        if (!enemyActionLog.isEmpty())   sb.append("▶ ").append(enemyActionLog).append("\n");
        if (battleOver)                  sb.append("\n★ ").append(endMessage);
        return sb.toString();
    }
}
