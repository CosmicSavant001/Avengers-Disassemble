package battle;

import characters.Hero;
import ai.EnemyAI;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a single battle encounter.
 * Both player and enemy are Hero instances.
 * Supports 3 skills per character.
 */
public class BattleManager {

    public enum PlayerAction { ATTACK, SKILL_1, SKILL_2, SKILL_3, DEFEND }

    private Hero         playerHero;
    private Hero         enemy;
    private EnemyAI      enemyAI;
    private int          turnNumber;
    private boolean      enemyStunned;
    private List<String> battleLog;

    public BattleManager(Hero hero, Hero enemy) {
        this.playerHero   = hero;
        this.enemy        = enemy;
        this.enemyAI      = new EnemyAI(enemy);
        this.turnNumber   = 1;
        this.enemyStunned = false;
        this.battleLog    = new ArrayList<>();
    }

    // ── Main Turn Execution ───────────────────────────────────────────────

    public BattleResult executeTurn(PlayerAction action) {
        BattleResult result = new BattleResult(turnNumber);

        // Player turn
        String playerLog = resolvePlayerAction(action);
        result.setPlayerActionLog(playerLog);
        battleLog.add("T" + turnNumber + " [Player]: " + playerLog);

        // Check win after player acts
        if (!enemy.isAlive()) {
            result.setBattleOver(true);
            result.setPlayerWon(true);
            result.setEndMessage(enemy.getName() + " defeated! +200 Score");
            playerHero.gainExperience(100);
            return result;
        }

        // Enemy turn
        if (enemyStunned) {
            result.setEnemyActionLog(enemy.getName() + " is STUNNED and cannot act!");
            enemyStunned = false;
        } else {
            String enemyLog = resolveEnemyTurn();
            result.setEnemyActionLog(enemyLog);
            battleLog.add("T" + turnNumber + " [Enemy]: " + enemyLog);
        }

        // Reset defend + tick cooldowns
        playerHero.resetDefend();
        playerHero.tickAllCooldowns();
        enemy.tickAllCooldowns();

        // Check loss
        if (!playerHero.isAlive()) {
            result.setBattleOver(true);
            result.setPlayerWon(false);
            result.setEndMessage(playerHero.getName() + " has fallen! GAME OVER.");
        }

        turnNumber++;
        return result;
    }

    // ── Player Action ─────────────────────────────────────────────────────

    private String resolvePlayerAction(PlayerAction action) {
        switch (action) {

            case ATTACK: {
                int dmg = playerHero.attack(enemy);
                return playerHero.getName() + " attacks " + enemy.getName()
                        + " for " + dmg + " damage!";
            }

            case SKILL_1: {
                if (playerHero.getSkill1() == null)
                    return playerHero.getName() + " has no Skill 1!";
                if (!playerHero.getSkill1().isReady())
                    return playerHero.getSkill1().getName()
                            + " is on cooldown! ("
                            + playerHero.getSkill1().getCooldownRemaining() + " turns)";
                if (playerHero.getCurrentMana() < playerHero.getSkill1().getManaCost())
                    return "Not enough Mana for " + playerHero.getSkill1().getName() + "!";
                int val = playerHero.useSkill1(enemy);
                checkStun(playerHero.getSkill1());
                return playerHero.getName() + " uses " + playerHero.getSkill1().getName()
                        + " for " + val + " damage!";
            }

            case SKILL_2: {
                if (playerHero.getSkill2() == null)
                    return playerHero.getName() + " has no Skill 2!";
                if (!playerHero.getSkill2().isReady())
                    return playerHero.getSkill2().getName()
                            + " is on cooldown! ("
                            + playerHero.getSkill2().getCooldownRemaining() + " turns)";
                if (playerHero.getCurrentMana() < playerHero.getSkill2().getManaCost())
                    return "Not enough Mana for " + playerHero.getSkill2().getName() + "!";
                int val = playerHero.useSkill2(enemy);
                return playerHero.getName() + " uses " + playerHero.getSkill2().getName()
                        + " for " + val + " damage!";
            }

            case SKILL_3: {
                if (playerHero.getSkill3() == null)
                    return playerHero.getName() + " has no Skill 3!";
                if (!playerHero.getSkill3().isReady())
                    return playerHero.getSkill3().getName()
                            + " is on cooldown! ("
                            + playerHero.getSkill3().getCooldownRemaining() + " turns)";
                if (playerHero.getCurrentMana() < playerHero.getSkill3().getManaCost())
                    return "Not enough Mana for " + playerHero.getSkill3().getName() + "!";
                int val = playerHero.useSkill3(enemy);
                return playerHero.getName() + " uses " + playerHero.getSkill3().getName()
                        + " for " + val + " damage!";
            }

            case DEFEND: {
                playerHero.defend();
                return playerHero.getName()
                        + " takes a defensive stance! (DEF doubled this turn)";
            }

            default:
                return "Unknown action.";
        }
    }

    // ── Enemy AI Turn ─────────────────────────────────────────────────────

    private String resolveEnemyTurn() {
        EnemyAI.AIAction aiAction = enemyAI.chooseAction();
        int dmg;
        switch (aiAction) {
            case USE_SKILL_1:
                if (enemy.getSkill1() != null && enemy.getSkill1().isReady()) {
                    dmg = enemy.useSkill1(playerHero);
                    return enemy.getName() + " uses " + enemy.getSkill1().getName()
                            + " for " + dmg + " damage!";
                }
                // fall through to attack if skill not ready
            case USE_SKILL_2:
                if (enemy.getSkill2() != null && enemy.getSkill2().isReady()) {
                    dmg = enemy.useSkill2(playerHero);
                    return enemy.getName() + " uses " + enemy.getSkill2().getName()
                            + " for " + dmg + " damage!";
                }
                // fall through to attack if skill not ready
            case ATTACK:
            default:
                dmg = enemy.attack(playerHero);
                return enemy.getName() + " attacks " + playerHero.getName()
                        + " for " + dmg + " damage!";
        }
    }

    // ── Stun Check ────────────────────────────────────────────────────────

    private void checkStun(abilities.Ability ability) {
        if (ability instanceof abilities.thor.LightningStrike strike) {
            if (strike.didStun()) {
                enemyStunned = true;
                battleLog.add("⚡ " + enemy.getName() + " is STUNNED!");
            }
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public Hero         getPlayerHero() { return playerHero; }
    public Hero         getEnemy()      { return enemy; }
    public int          getTurnNumber() { return turnNumber; }
    public List<String> getBattleLog()  { return new ArrayList<>(battleLog); }
    public boolean      isBattleOver()  { return !playerHero.isAlive() || !enemy.isAlive(); }
}