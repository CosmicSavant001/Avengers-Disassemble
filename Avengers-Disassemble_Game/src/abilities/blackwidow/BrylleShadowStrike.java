package abilities.blackwidow;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Brylle Shadow Strike | Damage: 180-250 */
public class BrylleShadowStrike extends Ability {

    public BrylleShadowStrike() {
        super("Brylle Shadow Strike",
              "A swift precise strike from the shadows. Damage: 180-250.",
              180, 2, 75);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 180 + (int)(Math.random() * 71); // 180-250
        // Ignores half defense
        int defOffset = target.getEffectiveDefense() / 2;
        target.takeDamage(damage + defOffset);
        System.out.println(user.getName() + " Shadow Strikes for " + damage + "!");
        return damage;
    }
}
