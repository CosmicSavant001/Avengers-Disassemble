package characters;

import abilities.loki.*;

public class Loki extends Hero {

    public Loki() {
        super("Loki", 3800, 26, 12, 750);
        setSkill1(new IllusionaryMirror());
        setSkill2(new ScepterEnergySurge());
        setSkill3(new GodOfMischiefDeception());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/loki.png");
    }

    @Override
    public String getDescription() {
        return "The God of Mischief who traps heroes inside the arena for " +
                "entertainment and manipulation. His illusions and deceptions " +
                "can confuse enemies and summon clones to fight for him.";
    }
}
