package abilities.cadie;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Raymund Paw Dip Ritual | Damage: 100-150 | Gradually heals */
public class PawDipRitual extends Ability {
    public PawDipRitual() {
        super("Paw Dip Ritual", "Cadie dips paw and licks. Damage: 100-150. Heals 150 HP.", 100, 2, 60);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 100 + (int)(Math.random() * 51);
        target.takeDamage(damage);
        user.heal(150);
        System.out.println("Cadie dips paw ritual for " + damage + "! Healed 150 HP!");
        return damage;
    }
}
