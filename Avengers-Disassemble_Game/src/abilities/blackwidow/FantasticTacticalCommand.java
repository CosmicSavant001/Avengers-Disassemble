package abilities.blackwidow;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Fantastic Tactical Command | Damage: 150-220 | Boosts team speed and evasion */
public class FantasticTacticalCommand extends Ability {
    public FantasticTacticalCommand() {
        super("Fantastic Tactical Command", "ULTIMATE! Team command strike. Damage: 150-220. Boosts speed and evasion.", 150, 4, 160);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 150 + (int)(Math.random() * 71);
        target.takeDamage(damage);
        user.heal(50);
        System.out.println(user.getName() + " Tactical Command for " + damage + "! Team evasion boosted!");
        return damage;
    }
}
