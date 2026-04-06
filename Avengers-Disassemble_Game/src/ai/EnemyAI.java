package ai;

import characters.Hero;
import java.util.Random;

/**
 * AI controller for enemy characters.
 * Randomly chooses between Attack, Skill1, or Skill2.
 */
public class EnemyAI {

    public enum AIAction { ATTACK, USE_SKILL_1, USE_SKILL_2 }

    private final Hero   enemy;
    private final Random random;

    public EnemyAI(Hero enemy) {
        this.enemy  = enemy;
        this.random = new Random();
    }

    public AIAction chooseAction() {
        boolean s1Ready = enemy.getSkill1() != null && enemy.getSkill1().isReady();
        boolean s2Ready = enemy.getSkill2() != null && enemy.getSkill2().isReady();
        boolean s3Ready = enemy.getSkill3() != null && enemy.getSkill3().isReady();

        // Low HP: prefer strongest available skill
        if (enemy.getHealthPercentage() < 0.30) {
            if (s3Ready) return AIAction.USE_SKILL_2; // use skill 3 mapped to skill 2 slot
            if (s1Ready) return AIAction.USE_SKILL_1;
        }

        double roll = random.nextDouble();
        if (s1Ready && roll < 0.30) return AIAction.USE_SKILL_1;
        if (s2Ready && roll < 0.50) return AIAction.USE_SKILL_2;
        return AIAction.ATTACK;
    }
}