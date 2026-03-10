package battle;

import abilities.LightningStrike;
import abilities.NatashaSting;
import characters.Character;
import characters.Enemy;
import characters.Hero;
import ai.EnemyAI;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the full lifecycle of a single battle encounter.
 * Handles turn order, action resolution, status effects, and battle end detection.
 */
public class BattleManager {

    // ─── Turn Actions (what the player can choose) ─────────────────────────
    public enum PlayerAction { ATTACK, USE_ABILITY, DEFEND }

    // ─── Battle State ──────────────────────────────────────────────────────
    private Hero   playerHero;
    private Enemy  enemy;
    private EnemyAI enemyAI;
    private int    turnNumber;
    private boolean enemyStunned;
    private List<String> battleLog;

    public BattleManager(Hero hero, Enemy enemy) {
        this.playerHero   = hero;
        this.enemy        = enemy;
        this.enemyAI      = new EnemyAI(enemy);
        this.turnNumber   = 1;
        this.enemyStunned = false;
        this.battleLog    = new ArrayList<>();
    }

    // ─── Main Battle Methods ───────────────────────────────────────────────

    /**
     * Executes the player's chosen action and then the enemy's AI turn.
     * @param action the player's chosen action
     * @return a BattleResult summarising what happened this round
     */
    public BattleResult executeTurn(PlayerAction action) {
        BattleResult result = new BattleResult(turnNumber);

        // ── Player's Turn ──
        String playerLog = resolvePlayerAction(action);
        result.setPlayerActionLog(playerLog);
        battleLog.add("TURN " + turnNumber + " [Player]: " + playerLog);

        // ── Check win condition after player acts ──
        if (!enemy.isAlive()) {
            result.setBattleOver(true);
            result.setPlayerWon(true);
            result.setEndMessage(buildVictoryMessage());
            awardVictory();
            return result;
        }

        // ── Tick status effects between turns ──
        tickStatusEffects(result);

        // ── Enemy's Turn ──
        if (enemyStunned) {
            result.setEnemyActionLog(enemy.getName() + " is stunned and cannot act!");
            enemyStunned = false;
        } else {
            String enemyLog = resolveEnemyTurn();
            result.setEnemyActionLog(enemyLog);
            battleLog.add("TURN " + turnNumber + " [Enemy ]: " + enemyLog);
        }

        // ── Reset defend stance ──
        playerHero.resetDefend();

        // ── Tick ability cooldowns ──
        if (playerHero.getSpecialAbility() != null) {
            playerHero.getSpecialAbility().tickCooldown();
        }

        // ── Check loss condition after enemy acts ──
        if (!playerHero.isAlive()) {
            result.setBattleOver(true);
            result.setPlayerWon(false);
            result.setEndMessage(playerHero.getName() + " has fallen! GAME OVER.");
        }

        turnNumber++;
        return result;
    }

    // ─── Private Resolution Helpers ───────────────────────────────────────

    private String resolvePlayerAction(PlayerAction action) {
        switch (action) {
            case ATTACK: {
                int dmg = playerHero.attack(enemy);
                return playerHero.getName() + " attacks " + enemy.getName()
                        + " for " + dmg + " damage!";
            }
            case USE_ABILITY: {
                if (playerHero.getSpecialAbility() == null) {
                    return playerHero.getName() + " has no special ability!";
                }
                if (!playerHero.getSpecialAbility().isReady()) {
                    return playerHero.getSpecialAbility().getName()
                            + " is on cooldown! (" + playerHero.getSpecialAbility().getCooldownRemaining() + " turns)";
                }
                int val = playerHero.useAbility(enemy);

                // Check Thor's stun effect
                if (playerHero.getSpecialAbility() instanceof LightningStrike) {
                    LightningStrike ls = (LightningStrike) playerHero.getSpecialAbility();
                    if (ls.didStun()) enemyStunned = true;
                }

                return playerHero.getName() + " uses " + playerHero.getSpecialAbility().getName()
                        + " on " + enemy.getName() + " for " + val + " damage!";
            }
            case DEFEND: {
                playerHero.defend();
                return playerHero.getName() + " takes a defensive stance! (DEF doubled)";
            }
            default:
                return "Unknown action.";
        }
    }

    private String resolveEnemyTurn() {
        EnemyAI.AIAction aiAction = enemyAI.chooseAction();
        int dmg;
        switch (aiAction) {
            case ATTACK:
                dmg = enemy.attack(playerHero);
                return enemy.getName() + " attacks " + playerHero.getName() + " for " + dmg + " damage!";
            case USE_ABILITY:
                if (enemy.getSpecialAbility() != null && enemy.getSpecialAbility().isReady()) {
                    dmg = enemy.useAbility(playerHero);
                    return enemy.getName() + " uses "
                            + enemy.getSpecialAbility().getName() + " for " + dmg + " damage!";
                } else {
                    dmg = enemy.attack(playerHero);
                    return enemy.getName() + " attacks " + playerHero.getName() + " for " + dmg + " damage!";
                }
            default:
                dmg = enemy.attack(playerHero);
                return enemy.getName() + " attacks for " + dmg + " damage!";
        }
    }

    private void tickStatusEffects(BattleResult result) {
        // Widow's Sting poison tick
        if (playerHero.getSpecialAbility() instanceof NatashaSting) {
            NatashaSting sting = (NatashaSting) playerHero.getSpecialAbility();
            if (sting.isPoisonActive()) {
                int poisonDmg = sting.tickPoison();
                if (poisonDmg > 0) {
                    result.appendStatusEffect(enemy.getName()
                            + " takes " + poisonDmg + " poison damage! ("
                            + sting.getPoisonTurnsLeft() + " turns remaining)");
                }
            }
        }
    }

    private void awardVictory() {
        if (playerHero instanceof characters.Hero) {
            ((Hero) playerHero).gainExperience(enemy.getXpReward());
        }
    }

    private String buildVictoryMessage() {
        return enemy.getName() + " has been defeated! +" + enemy.getXpReward()
                + " XP | +" + enemy.getScoreReward() + " Score";
    }

    // ─── Getters ──────────────────────────────────────────────────────────

    public Hero getPlayerHero()      { return playerHero; }
    public Enemy getEnemy()          { return enemy; }
    public int getTurnNumber()       { return turnNumber; }
    public List<String> getBattleLog() { return new ArrayList<>(battleLog); }
    public boolean isBattleOver()    { return !playerHero.isAlive() || !enemy.isAlive(); }
}
