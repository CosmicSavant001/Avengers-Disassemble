package abilities.doctorstrange;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Brylle Mystic Guard | Damage: 140-200 | Effect: Shield */
public class MysticGuard extends Ability {

    public MysticGuard() {
        super("Mystic Guard",
              "Creates a mystic shield. Damage: 140-200. Absorbs next hit.",
              140, 2, 85);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 140 + (int)(Math.random() * 61); // 140-200
        target.takeDamage(damage);
        // Activate shield on user — doubles defense this turn
        user.defend();
        System.out.println(user.getName() + " casts Mystic Guard for "
                + damage + " and raises a protective shield!");
        return damage;
    }
}
