package abilities.ironman;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Brylle Reactor Strike | Damage: 160-250 */
public class ReactorStrike extends Ability {

    public ReactorStrike() {
        super("Reactor Strike",
              "Fires a concentrated repulsor energy beam. Damage: 160-250",
              160, 2, 80);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 160 + (int)(Math.random() * 91); // 160-250
        int defPen = (int)(target.getDefensePower() * 0.5);
        target.takeDamage(damage + defPen);
        int actual = damage;
        System.out.println(user.getName() + " fires Brylle Reactor Strike for " + actual + "!");
        return actual;
    }
}
