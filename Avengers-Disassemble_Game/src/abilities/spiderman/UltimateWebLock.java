package abilities.spiderman;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Patagnan Ultimate Web Lock | Damage: 320-470 | Reduces speed and accuracy */
public class UltimateWebLock extends Ability {
    public UltimateWebLock() {
        super("Ultimate Web Lock", "ULTIMATE! Full web trap. Damage: 320-470. Reduces enemy speed and accuracy.", 320, 4, 180);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 320 + (int)(Math.random() * 151);
        target.takeDamage(damage);
        System.out.println(user.getName() + " Web Locks " + target.getName() + " for " + damage + "! Enemy slowed!");
        return damage;
    }
}
