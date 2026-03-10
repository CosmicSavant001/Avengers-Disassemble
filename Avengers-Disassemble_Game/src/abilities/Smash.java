package abilities;

import characters.Character;

/**
 * Hulk's earth-shaking ability – a ground-pound that deals massive AOE damage.
 * Damage scales based on Hulk's missing health (angrier = stronger).
 */
public class Smash extends Ability {

    public Smash() {
        super("Smash",
              "Hulk smashes the ground – damage increases as Hulk loses health.",
              45, 3);
        this.soundFile = "assets/audio/smash.wav";
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        // Rage bonus: more damage the lower Hulk's HP percentage
        double missingHpRatio = 1.0 - user.getHealthPercentage();
        int rageBonus = (int)(missingHpRatio * 20); // up to +20 bonus damage

        int damage = Math.max(1, baseDamage + rageBonus - target.getDefensePower() / 2);
        target.takeDamage(damage);

        System.out.println("HULK SMASH! " + user.getName() + " deals " + damage
                + " damage!" + (rageBonus > 0 ? " (+" + rageBonus + " RAGE BONUS)" : ""));
        return damage;
    }
}
