package abilities.ultron;

import abilities.Ability;
import characters.Character;

public class ExtinctionCommand extends Ability {

    public ExtinctionCommand() {
        super("Extinction Command", "Devastating all-enemy attack that lowers enemy defense.",
                380, 5, 200);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 170); // 380-550
        target.takeDamage(damage);
        System.out.println("EXTINCTION COMMAND! " + target.getName()
                + "'s defenses are shattered!");
        return damage;
    }
}