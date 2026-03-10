package abilities;

import characters.Character;

/**
 * Captain America's iconic move – hurls his vibranium shield.
 * Deals physical damage that ignores ALL enemy defense.
 */
public class ShieldThrow extends Ability {

    public ShieldThrow() {
        super("Shield Throw",
              "Hurls the vibranium shield, ignoring all enemy defense.",
              30, 2);
        this.soundFile = "assets/audio/shield_throw.wav";
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        // Shield throw completely ignores defense
        int damage = baseDamage + user.getAttackPower() / 3;

        // Direct health reduction, bypassing defense entirely
        int before = target.getCurrentHealth();
        // We apply damage directly by healing then dealing (hack-free approach):
        // Instead we just call the protected field indirectly via heal trick:
        // Simplest approach – set health manually through the takeDamage workaround:
        // Force raw damage: call takeDamage with extra to cancel defense offset
        int defenseOffset = target.getEffectiveDefense();
        target.takeDamage(damage + defenseOffset); // net effect = damage
        int actualDamage = before - target.getCurrentHealth();

        System.out.println(user.getName() + " throws his shield for " + actualDamage
                + " piercing damage! (defense ignored)");
        return actualDamage;
    }
}
