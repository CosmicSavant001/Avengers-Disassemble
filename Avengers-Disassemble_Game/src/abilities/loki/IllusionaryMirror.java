package abilities.loki;

import abilities.Ability;
import characters.Character;

public class IllusionaryMirror extends Ability {

    public IllusionaryMirror() {
        super("Illusionary Mirror", "Creates a temporary illusion that repeats an attack.",
                220, 3, 80);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 100); // 220-320
        target.takeDamage(damage);
        // Repeat attack at half damage
        int echo = damage / 2;
        target.takeDamage(echo);
        System.out.println("The illusion echoes the strike for " + echo + " bonus damage!");
        return damage + echo;
    }
}