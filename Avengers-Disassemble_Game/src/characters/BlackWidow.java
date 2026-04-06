package characters;

import abilities.blackwidow.*;

public class BlackWidow extends Hero {

    public BlackWidow() {
        super("Black Widow", 2000, 20, 9, 450);
        setSkill1(new BrylleShadowStrike());
        setSkill2(new SilentExecution());
        setSkill3(new FantasticTacticalCommand());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/black_widow.png");
    }

    @Override
    public String getDescription() {
        return "A master tactician who uses strategy and precision to outsmart " +
                "cloned opponents. Her Shadow Strike deals heavy damage, and her " +
                "Tactical Command boosts the whole team's speed and evasion.";
    }

    @Override
    public void takeDamage(int rawDamage) {
        if (Math.random() < 0.10) {
            System.out.println(name + " evades the attack!");
            return;
        }
        super.takeDamage(rawDamage);
    }
}