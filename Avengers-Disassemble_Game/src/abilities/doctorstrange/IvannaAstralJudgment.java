package abilities.doctorstrange;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Ivanna Astral Judgment | Damage: 320-480 | Ignores all defense */
public class IvannaAstralJudgment extends Ability {
    public IvannaAstralJudgment() {
        super("Ivanna Astral Judgment", "ULTIMATE! Astral strike that IGNORES all defense. Damage: 320-480.", 320, 4, 200);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 320 + (int)(Math.random() * 161);
        // Ignore all defense - apply directly
        int before = target.getCurrentHealth();
        target.heal(target.getEffectiveDefense()); // cancel defense offset
        target.takeDamage(damage + target.getEffectiveDefense());
        System.out.println(user.getName() + " ASTRAL JUDGMENT for " + damage + "! Defense IGNORED!");
        return damage;
    }
}
