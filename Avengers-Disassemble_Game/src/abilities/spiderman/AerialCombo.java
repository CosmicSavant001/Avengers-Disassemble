package abilities.spiderman;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Lucy Kate Aerial Combo | Damage: 260-390 | High critical chance */
public class AerialCombo extends Ability {
    public AerialCombo() {
        super("Aerial Combo", "Aerial web combo with high critical chance. Damage: 260-390.", 260, 3, 120);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 260 + (int)(Math.random() * 131);
        boolean crit = Math.random() < 0.40;
        if (crit) { damage = (int)(damage * 1.5); System.out.println("CRITICAL HIT!"); }
        target.takeDamage(damage);
        System.out.println(user.getName() + " Aerial Combo for " + damage + "!" + (crit ? " (CRIT!)" : ""));
        return damage;
    }
}
