package abilities;

import characters.Character;

/**
 * Iron Man's signature ability – fires a concentrated repulsor energy beam.
 * Deals high energy damage that partially bypasses enemy defense.
 */
public class RepulsorBlast extends Ability {

    private static final double DEFENSE_PENETRATION = 0.5; // ignores 50% of target defense

    public RepulsorBlast() {
        super("Repulsor Blast",
              "Fires a concentrated energy beam that partially bypasses enemy armor.",
              35, 2);
        this.soundFile = "assets/audio/repulsor_blast.wav";
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        // Calculate damage with partial defense penetration
        int targetDefense = (int)(target.getDefensePower() * DEFENSE_PENETRATION);
        int damage = Math.max(1, baseDamage + user.getAttackPower() / 2 - targetDefense);

        // Apply damage directly (bypass normal takeDamage defense calc)
        int rawHp = target.getCurrentHealth();
        target.takeDamage(damage + targetDefense); // offset so net = damage
        int actualDamage = rawHp - target.getCurrentHealth();

        System.out.println(user.getName() + " fires a Repulsor Blast for " + actualDamage + " damage!");
        return actualDamage;
    }
}
