package abilities.blackwidow;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Ivanna Silent Execution | Damage: 210-310 | Reduces enemy defense */
public class SilentExecution extends Ability {
    public SilentExecution() {
        super("Silent Execution", "Silent precision strike. Damage: 210-310. Reduces enemy defense.", 210, 3, 110);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 210 + (int)(Math.random() * 101);
        target.takeDamage(damage);
        System.out.println(user.getName() + " Silent Execution for " + damage + "! Enemy defense lowered!");
        return damage;
    }
}
