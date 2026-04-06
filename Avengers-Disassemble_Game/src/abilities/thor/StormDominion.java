package abilities.thor;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Lucy Storm Dominion | Damage: 420-560 (All Enemies) */
public class StormDominion extends Ability {
    public StormDominion() {
        super("Storm Dominion", "ULTIMATE! Full storm dominion. Damage: 420-560!", 420, 4, 200);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 420 + (int)(Math.random() * 141);
        target.takeDamage(damage);
        System.out.println("STORM DOMINION! " + damage + " damage! THE SKIES RAGE!");
        return damage;
    }
}
