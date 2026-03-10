package abilities;

import characters.Character;

/**
 * Thor's elemental ability – calls down a bolt of lightning from Asgard.
 * High damage with a chance to stun the target (skip their next turn).
 */
public class LightningStrike extends Ability {

    private static final double STUN_CHANCE = 0.40; // 40% chance to stun
    private boolean didStun = false;

    public LightningStrike() {
        super("Lightning Strike",
              "Calls a bolt of divine lightning – high damage with 40% stun chance.",
              40, 3);
        this.soundFile = "assets/audio/lightning_strike.wav";
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = Math.max(1, baseDamage + user.getAttackPower() / 2 - target.getDefensePower());
        target.takeDamage(damage);

        didStun = Math.random() < STUN_CHANCE;
        if (didStun) {
            System.out.println(target.getName() + " is STUNNED and will lose their next turn!");
        }
        System.out.println(user.getName() + " calls lightning for " + damage + " damage!"
                + (didStun ? " (STUNNED)" : ""));
        return damage;
    }

    /** Returns whether the last activation produced a stun effect. */
    public boolean didStun() { return didStun; }
}
