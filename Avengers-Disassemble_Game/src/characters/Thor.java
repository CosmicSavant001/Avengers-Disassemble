package characters;

import abilities.thor.*;

public class Thor extends Hero {

    public Thor() {
        super("Thor", 2700, 25, 10, 400);
        setSkill1(new LightningStrike());
        setSkill2(new ThunderCrash());
        setSkill3(new StormDominion());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/thor.png");
    }

    @Override
    public String getDescription() {
        return "The God of Thunder enters the arena to stop Loki's chaos " +
                "and Ultron's technological threat. His lightning strikes can " +
                "stun enemies and devastate all foes with Storm Dominion.";
    }
}