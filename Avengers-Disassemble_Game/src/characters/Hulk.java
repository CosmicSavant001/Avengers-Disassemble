package characters;

import abilities.Smash;

/**
 * Hulk – The Strongest One There Is.
 * Highest health and attack, lowest defense, but Smash deals earth-shaking damage.
 */
public class Hulk extends Hero {

    private boolean enraged = false;
    private static final int ENRAGE_THRESHOLD = 40; // % HP

    public Hulk() {
        super("Hulk", 180, 28, 5);
        setSpecialAbility(new Smash());
        setImagePath("assets/images/heroes/hulk.png");
    }

    @Override
    public String getDescription() {
        return "HULK SMASH! The madder Hulk gets, the stronger he gets. " +
               "Hulk has the most raw health and attack in the roster. " +
               "When his health drops low, he enters a rage state for extra power.";
    }

    /**
     * When below the enrage threshold, attack power gets a boost.
     */
    @Override
    public void takeDamage(int rawDamage) {
        super.takeDamage(rawDamage);
        checkEnrage();
    }

    private void checkEnrage() {
        boolean shouldEnrage = getHealthPercentage() * 100 < ENRAGE_THRESHOLD;
        if (shouldEnrage && !enraged) {
            enraged = true;
            attackPower += 8;
            System.out.println("HULK ENRAGED! Attack power surges!");
        } else if (!shouldEnrage && enraged) {
            enraged = false;
            attackPower -= 8;
        }
    }

    public boolean isEnraged() { return enraged; }
}
