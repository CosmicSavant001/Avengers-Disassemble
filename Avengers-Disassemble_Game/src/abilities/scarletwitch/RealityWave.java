package abilities.scarletwitch;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Embodo Reality Wave | Damage: 230-360 (All Enemies) */
public class RealityWave extends Ability {
    public RealityWave() {
        super("Reality Wave", "Reality warping wave! Damage: 230-360. Hits all enemies.", 230, 3, 130);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 230 + (int)(Math.random() * 131);
        target.takeDamage(damage);
        System.out.println(user.getName() + " Reality Wave for " + damage + "!");
        return damage;
    }
}
