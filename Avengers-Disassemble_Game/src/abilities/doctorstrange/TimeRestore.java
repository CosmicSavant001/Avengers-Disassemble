package abilities.doctorstrange;
import abilities.Ability;
import characters.Character;
/** Skill 2 - Alerta Time Restore | Damage: 180-260 | Heals the user */
public class TimeRestore extends Ability {
    public TimeRestore() {
        super("Time Restore", "Time magic strike and self heal. Damage: 180-260. Heals 200 HP.", 180, 3, 110);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 180 + (int)(Math.random() * 81);
        target.takeDamage(damage);
        user.heal(200);
        System.out.println(user.getName() + " Time Restore for " + damage + "! Healed 200 HP!");
        return damage;
    }
}
