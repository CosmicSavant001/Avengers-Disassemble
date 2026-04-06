package characters;

import abilities.ultron.*;

public class Ultron extends Hero {

    public Ultron() {
        super("Ultron", 4200, 30, 15, 600);
        setSkill1(new VibraniumBeam());
        setSkill2(new DroneReplicationProtocol());
        setSkill3(new ExtinctionCommand());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/ultron.png");
    }

    @Override
    public String getDescription() {
        return "The rogue AI who creates perfect clones, believing machines " +
                "are superior to humans. His Vibranium Beam and Extinction " +
                "Command can devastate all enemies at once.";
    }
}
