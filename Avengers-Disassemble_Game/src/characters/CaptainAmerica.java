package characters;

import abilities.ShieldThrow;

/**
 * Captain America – Steve Rogers, the First Avenger.
 * Highest defense, balanced attack, Shield Throw ricochets for bonus damage.
 */
public class CaptainAmerica extends Hero {

    public CaptainAmerica() {
        super("Captain America", 140, 18, 14);
        setSpecialAbility(new ShieldThrow());
        setImagePath("assets/images/heroes/captain_america.png");
    }

    @Override
    public String getDescription() {
        return "The living legend. Captain America's vibranium shield " +
               "provides unmatched protection, and his Shield Throw " +
               "ricochets to deal piercing damage that ignores defense.";
    }

    /**
     * Cap's defend is extra effective – the shield provides additional coverage.
     */
    @Override
    public void defend() {
        super.defend();
        // Bonus: temporarily boost defense while defending
        this.defensePower += 4;
    }

    @Override
    public void resetDefend() {
        if (isDefending()) {
            this.defensePower -= 4;
        }
        super.resetDefend();
    }
}
