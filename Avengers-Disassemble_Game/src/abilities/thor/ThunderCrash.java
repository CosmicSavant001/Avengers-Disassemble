package abilities.thor;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Tubiano Thunder Crash | Damage: 210-330 (All Enemies) */
public class ThunderCrash extends Ability {
    public ThunderCrash() {
        super("Thunder Crash", "Thunder crashes down! Damage: 210-330. Hits all enemies.", 210, 3, 120);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 210 + (int)(Math.random() * 121);
        target.takeDamage(damage);
        System.out.println(user.getName() + " Thunder Crash for " + damage + "!");
        return damage;
    }
}
