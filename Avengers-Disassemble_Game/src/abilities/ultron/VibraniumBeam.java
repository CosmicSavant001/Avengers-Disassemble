package abilities.ultron;

import abilities.Ability;
import characters.Character;

public class VibraniumBeam extends Ability {

    public VibraniumBeam() {
        super("Vibranium Beam", "Fires a concentrated beam of Vibranium energy.",
                240, 3, 90);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 100); // 240-340
        target.takeDamage(damage);
        System.out.println("Ultron fires a Vibranium Beam at " + target.getName() + "!");
        return damage;
    }
}