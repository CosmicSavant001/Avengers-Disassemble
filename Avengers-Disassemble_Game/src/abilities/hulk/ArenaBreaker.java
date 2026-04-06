package abilities.hulk;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Alerta Arena Breaker | Damage: 220-320 (All Enemies) */
public class ArenaBreaker extends Ability {
    public ArenaBreaker() {
        super("Arena Breaker", "Hulk breaks the arena! Damage: 220-320. Hits all enemies.", 220, 3, 120);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 220 + (int)(Math.random() * 101);
        target.takeDamage(damage);
        System.out.println("HULK BREAKS THE ARENA! " + damage + " damage!");
        return damage;
    }
}
