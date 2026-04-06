package abilities.loki;

import abilities.Ability;
import characters.Character;

public class GodOfMischiefDeception extends Ability {

    public GodOfMischiefDeception() {
        super("God of Mischief's Deception", "Summons a clone of the enemy that fights for several turns.",
                350, 5, 150);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 150); // 350-500
        target.takeDamage(damage);
        System.out.println("Loki summons a clone of " + target.getName()
                + " to haunt them for 2 turns!");
        return damage;
    }
}