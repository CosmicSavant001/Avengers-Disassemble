package abilities.cadie;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Alerta Direct Drink Awakening | Damage: 180-260 | Restores large HP and increases defense */
public class DirectDrinkAwakening extends Ability {
    public DirectDrinkAwakening() {
        super("Alerta Direct Drink Awakening", "ULTIMATE! Cadie drinks directly! Damage: 180-260. Restores 400 HP and boosts defense!", 180, 4, 140);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 180 + (int)(Math.random() * 81);
        target.takeDamage(damage);
        user.heal(400);
        System.out.println("CADIE DIRECT DRINK AWAKENING! " + damage + " damage! Restored 400 HP!");
        return damage;
    }
}
