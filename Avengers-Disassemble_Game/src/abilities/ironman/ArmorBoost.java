package abilities.ironman;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Embodo Armor Boost | Damage: 120-180 | Boosts attack for 2 turns */
public class ArmorBoost extends Ability {
    public ArmorBoost() {
        super("Armor Boost", "Armor boost! Damage: 120-180. Attack power increases for 2 turns.", 120, 3, 90);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 120 + (int)(Math.random() * 61);
        target.takeDamage(damage);
        user.restoreMana(20);
        System.out.println(user.getName() + " uses Embodo Armor Boost for " + damage + "! Attack boosted!");
        return damage;
    }
}
