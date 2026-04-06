package abilities.hulk;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Raymund Power Smash | Damage: 190-270 */
public class PowerSmash extends Ability {

    public PowerSmash() {
        super("Power Smash",
              "Hulk smashes with full force! Damage: 190-270.",
              190, 2, 80);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        double missingHp  = 1.0 - user.getHealthPercentage();
        int    rageBonus  = (int)(missingHp * 80);
        int    damage     = 190 + (int)(Math.random() * 81) + rageBonus; // 190-270 + rage
        target.takeDamage(damage);
        System.out.println("HULK SMASH! " + damage + " damage!"
                + (rageBonus > 0 ? " (+" + rageBonus + " RAGE)" : ""));
        return damage;
    }
}
