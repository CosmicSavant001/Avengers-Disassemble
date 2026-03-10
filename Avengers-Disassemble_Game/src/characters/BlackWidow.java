package characters;

import abilities.NatashaSting;

/**
 * Black Widow – Natasha Romanoff, master spy and assassin.
 * Low health but the fastest character; her Widow's Sting poisons enemies.
 */
public class BlackWidow extends Hero {

    public BlackWidow() {
        super("Black Widow", 100, 20, 9);
        setSpecialAbility(new NatashaSting());
        setImagePath("assets/images/heroes/black_widow.png");
    }

    @Override
    public String getDescription() {
        return "Whatever it takes. Natasha Romanoff's Widow's Sting delivers " +
               "an electroshock that poisons enemies, draining their health " +
               "over multiple turns. Fragile but deadly.";
    }

    /**
     * Black Widow has a chance to evade incoming attacks (10% base).
     */
    @Override
    public void takeDamage(int rawDamage) {
        if (Math.random() < 0.10) {
            System.out.println(name + " evades the attack!");
            return;
        }
        super.takeDamage(rawDamage);
    }
}
