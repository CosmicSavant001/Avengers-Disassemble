package abilities.cadie;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Vince's Six-Hour Hunger Call | Damage: 150-230 | Attack boost after turns */
public class VinceSixHourHungerCall extends Ability {

    public VinceSixHourHungerCall() {
        super("Vince's Six-Hour Hunger Call",
              "Cadie yowls for food! Damage: 150-230. Gains attack boost after turns.",
              150, 2, 60);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 150 + (int)(Math.random() * 81); // 150-230
        target.takeDamage(damage);

        // Boost Cadie's attack temporarily
        if (user instanceof characters.Cadie) {
            ((characters.Cadie) user).resetHunger();
        }
        System.out.println("Cadie unleashes a HUNGER CALL for "
                + damage + " damage! Attack boosted!");
        return damage;
    }
}
