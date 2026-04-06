package characters;

import abilities.ironman.*;

public class IronMan extends Hero {

    public IronMan() {
        super("Iron Man", 2400, 22, 8, 500);
        setSkill1(new ReactorStrike());
        setSkill2(new ArmorBoost());
        setSkill3(new FantasticFusionBarrage());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/ironman.png");
    }

    @Override
    public String getDescription() {
        return "Genius billionaire Tony Stark upgrades his armor to survive " +
                "the Multiversal Clash Arena and stop Ultron's cloning system. " +
                "His Repulsor tech deals massive energy damage.";
    }
}