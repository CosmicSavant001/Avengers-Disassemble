package abilities.thor;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Ivanna Lightning Strike | Damage: 170-250 */
public class LightningStrike extends Ability {

    private boolean didStun = false;

    public LightningStrike() {
        super("Lightning Strike",
              "Calls divine lightning from Asgard. Damage: 170-250. 40% stun chance.",
              170, 2, 90);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 170 + (int)(Math.random() * 81); // 170-250
        target.takeDamage(damage);
        didStun = Math.random() < 0.40;
        System.out.println(user.getName() + " strikes with lightning for "
                + damage + "!" + (didStun ? " TARGET STUNNED!" : ""));
        return damage;
    }

    public boolean didStun() { return didStun; }
}
