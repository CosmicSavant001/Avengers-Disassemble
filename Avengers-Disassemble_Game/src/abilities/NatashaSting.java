package abilities;

import characters.Character;

/**
 * Black Widow's electroshock baton strike – the Widow's Sting.
 * Deals moderate damage and applies a poison effect for 3 turns.
 */
public class NatashaSting extends Ability {

    private static final int POISON_DAMAGE   = 8;
    private static final int POISON_DURATION = 3;

    // Track poison state externally (BattleManager reads these)
    private boolean poisonApplied = false;
    private int     poisonTurnsLeft = 0;
    private Character poisonedTarget = null;

    public NatashaSting() {
        super("Widow's Sting",
              "Electro-shock that deals damage and poisons the target for 3 turns.",
              20, 3);
        this.soundFile = "assets/audio/widows_sting.wav";
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = Math.max(1, baseDamage - target.getDefensePower() / 2);
        target.takeDamage(damage);

        // Apply poison
        poisonApplied   = true;
        poisonTurnsLeft = POISON_DURATION;
        poisonedTarget  = target;

        System.out.println(user.getName() + " uses Widow's Sting for " + damage
                + " damage and poisons " + target.getName() + " for "
                + POISON_DURATION + " turns!");
        return damage;
    }

    /**
     * Should be called at the end of each enemy turn to apply poison tick.
     * @return poison damage dealt this tick, 0 if not poisoned.
     */
    public int tickPoison() {
        if (poisonTurnsLeft <= 0 || poisonedTarget == null) return 0;

        poisonedTarget.takeDamage(POISON_DAMAGE);
        poisonTurnsLeft--;
        System.out.println(poisonedTarget.getName() + " takes " + POISON_DAMAGE
                + " poison damage! (" + poisonTurnsLeft + " turns left)");

        if (poisonTurnsLeft == 0) {
            poisonApplied  = false;
            poisonedTarget = null;
        }
        return POISON_DAMAGE;
    }

    public boolean isPoisonActive()   { return poisonApplied && poisonTurnsLeft > 0; }
    public int     getPoisonTurnsLeft() { return poisonTurnsLeft; }
}
