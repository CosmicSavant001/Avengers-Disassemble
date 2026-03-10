package ai;

import characters.Enemy;
import java.util.Random;

/**
 * Simple AI controller for enemy characters.
 * Chooses between ATTACK and USE_ABILITY based on weighted random logic.
 * Ability is preferred when available (60/40 split).
 */
public class EnemyAI {

    public enum AIAction { ATTACK, USE_ABILITY }

    private final Enemy enemy;
    private final Random random;

    // Tuning weights
    private static final double ABILITY_PREFERENCE = 0.60; // 60% prefer ability when ready

    public EnemyAI(Enemy enemy) {
        this.enemy  = enemy;
        this.random = new Random();
    }

    /**
     * Decides the next action for the enemy.
     * - If ability is ready: 60% chance to use it, 40% chance to attack.
     * - If ability is on cooldown: always attacks.
     */
    public AIAction chooseAction() {
        boolean abilityReady = enemy.getSpecialAbility() != null
                && enemy.getSpecialAbility().isReady();

        if (abilityReady && random.nextDouble() < ABILITY_PREFERENCE) {
            return AIAction.USE_ABILITY;
        }
        return AIAction.ATTACK;
    }

    /**
     * Smarter version that factors in HP thresholds.
     * When the enemy is below 30% HP it always uses ability if available.
     */
    public AIAction chooseActionSmart() {
        boolean abilityReady = enemy.getSpecialAbility() != null
                && enemy.getSpecialAbility().isReady();
        boolean lowHealth = enemy.getHealthPercentage() < 0.30;

        if (abilityReady && (lowHealth || random.nextDouble() < ABILITY_PREFERENCE)) {
            return AIAction.USE_ABILITY;
        }
        return AIAction.ATTACK;
    }
}
