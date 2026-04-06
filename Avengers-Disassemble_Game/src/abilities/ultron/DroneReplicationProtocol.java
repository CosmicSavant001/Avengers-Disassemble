package abilities.ultron;

import abilities.Ability;
import characters.Character;

public class DroneReplicationProtocol extends Ability {

    public DroneReplicationProtocol() {
        super("Drone Replication Protocol", "Summons drone allies to attack the enemy.",
                200, 3, 80);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = baseDamage + (int)(Math.random() * 80); // 200-280
        target.takeDamage(damage);
        // Drones deal additional hits
        int droneDamage = 50 + (int)(Math.random() * 30);
        target.takeDamage(droneDamage);
        System.out.println("Ultron's drones swarm " + target.getName()
                + " for an additional " + droneDamage + " damage!");
        return damage + droneDamage;
    }
}