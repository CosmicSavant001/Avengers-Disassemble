package characters;

import abilities.spiderman.*;

public class SpiderMan extends Hero {

    public SpiderMan() {
        super("Spider-Man", 2100, 20, 9, 450);
        setSkill1(new KatePrecisionWeb());
        setSkill2(new AerialCombo());
        setSkill3(new UltimateWebLock());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/spiderman.png");
    }

    @Override
    public String getDescription() {
        return "Pulled into the arena, Spider-Man relies on agility and " +
                "intelligence to outmaneuver his own clone and protect the Blue Team. " +
                "His web attacks can immobilize enemies.";
    }

    @Override
    public void takeDamage(int rawDamage) {
        if (Math.random() < 0.15) {
            System.out.println(name + " dodges with Spider-Sense!");
            return;
        }
        super.takeDamage(rawDamage);
    }
}