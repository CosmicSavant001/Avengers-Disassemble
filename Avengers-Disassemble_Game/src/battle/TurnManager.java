package battle;

/**
 * Tracks whose turn it is and manages turn order state.
 * Supports simple two-party turn tracking (Player vs Enemy).
 */
public class TurnManager {

    public enum TurnOwner { PLAYER, ENEMY }

    private TurnOwner currentTurn;
    private int totalTurns;

    public TurnManager() {
        this.currentTurn = TurnOwner.PLAYER; // player always goes first
        this.totalTurns  = 0;
    }

    /** Advances to the next turn and returns the new turn owner. */
    public TurnOwner nextTurn() {
        totalTurns++;
        currentTurn = (currentTurn == TurnOwner.PLAYER)
                ? TurnOwner.ENEMY
                : TurnOwner.PLAYER;
        return currentTurn;
    }

    public TurnOwner getCurrentTurn() { return currentTurn; }
    public int       getTotalTurns()  { return totalTurns;  }
    public boolean   isPlayerTurn()   { return currentTurn == TurnOwner.PLAYER; }
}
