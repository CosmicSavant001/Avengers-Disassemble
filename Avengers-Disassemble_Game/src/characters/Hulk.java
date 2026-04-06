package characters;

import abilities.hulk.*;

public class Hulk extends Hero {

    private boolean enraged = false;
    private static final int ENRAGE_HP_PERCENT = 40;

    public Hulk() {
        super("Hulk", 3500, 28, 5, 300);
        setSkill1(new PowerSmash());
        setSkill2(new ArenaBreaker());
        setSkill3(new UltimateFury());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/hulk.png");
    }

    @Override
    public String getDescription() {
        return "Fueled by rage, Hulk battles his clone to prove that true " +
                "strength comes from heart, not programming. When his HP drops " +
                "below 40% he enters RAGE mode for a massive attack boost!";
    }

    @Override
    public void takeDamage(int rawDamage) {
        super.takeDamage(rawDamage);
        checkEnrage();
    }

    private void checkEnrage() {
        boolean shouldEnrage = getHealthPercentage() * 100 < ENRAGE_HP_PERCENT;
        if (shouldEnrage && !enraged) {
            enraged = true;
            attackPower += 40;
            System.out.println("HULK ENRAGED! Attack surges by 40!");
        } else if (!shouldEnrage && enraged) {
            enraged = false;
            attackPower -= 40;
        }
    }

    public boolean isEnraged() { return enraged; }
}