package characters;

import abilities.scarletwitch.*;

public class ScarletWitch extends Hero {

    public ScarletWitch() {
        super("Scarlet Witch", 2200, 19, 7, 600);
        setSkill1(new ChaosBurst());
        setSkill2(new RealityWave());
        setSkill3(new DistortionField());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/scarlet_witch.png");
    }

    @Override
    public String getDescription() {
        return "Using chaos magic, Wanda senses the imbalance caused by cloning " +
                "and fights to restore reality. Her reality-warping powers can " +
                "lower enemy attack and confuse all enemies.";
    }
}