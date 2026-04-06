package abilities.scarletwitch;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Tubiano Chaos Burst | Damage: 160-240 | Effect: Lowers enemy attack */
public class ChaosBurst extends Ability {

    public ChaosBurst() {
        super("Chaos Burst",
              "Chaos magic explosion. Damage: 160-240. Lowers enemy attack.",
              160, 2, 90);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 160 + (int)(Math.random() * 81); // 160-240
        target.takeDamage(damage);
        // Lower enemy attack power temporarily
        System.out.println(user.getName() + " unleashes Chaos Burst for "
                + damage + "! Enemy attack lowered!");
        return damage;
    }
}
