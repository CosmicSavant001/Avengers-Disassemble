package abilities.scarletwitch;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Fantastic Distortion Field | Damage: 280-420 | Confuses all enemies for 1 turn */
public class DistortionField extends Ability {
    public DistortionField() {
        super("Distortion Field", "ULTIMATE! Reality distortion. Damage: 280-420. Confuses all enemies!", 280, 4, 200);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 280 + (int)(Math.random() * 141);
        target.takeDamage(damage);
        System.out.println(user.getName() + " Distortion Field for " + damage + "! Enemies CONFUSED!");
        return damage;
    }
}
