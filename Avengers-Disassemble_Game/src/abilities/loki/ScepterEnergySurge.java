package abilities.loki;

import abilities.Ability;
import characters.Character;

public class ScepterEnergySurge extends Ability {

    public ScepterEnergySurge() {
        super("Scepter Energy Surge", "Blasts all enemies with scepter energy. Chance to stun.",
                300, 4, 120);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 120); // 300-420
        target.takeDamage(damage);
        if (Math.random() < 0.40) {
            System.out.println(target.getName() + " is STUNNED by the Scepter Energy Surge!");
        }
        return damage;
    }
}