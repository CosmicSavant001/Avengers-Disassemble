package characters;

import abilities.cadie.*;

public class Cadie extends Hero {

    private int turnsWithoutEating = 0;
    private static final int HUNGER_THRESHOLD = 3;

    public Cadie() {
        super("Cadie", 2600, 17, 10, 350);
        setSkill1(new VinceSixHourHungerCall());
        setSkill2(new PawDipRitual());
        setSkill3(new DirectDrinkAwakening());
        setSpecialAbility(getSkill1());
        setImagePath("assets/images/heroes/cadie.png");
    }

    @Override
    public String getDescription() {
        return "A chubby orange Flerken cat who unexpectedly joins the fight! " +
                "After eating, Cadie becomes stronger. His unique way of drinking " +
                "water unlocks powerful healing abilities.";
    }

    @Override
    public int attack(Character target) {
        turnsWithoutEating++;
        int bonus = turnsWithoutEating >= HUNGER_THRESHOLD ? 30 : 0;
        if (bonus > 0)
            System.out.println("Cadie is HUNGRY and ANGRY! +" + bonus + " attack bonus!");
        int dmg = Math.max(1, (attackPower + bonus) - target.getEffectiveDefense());
        target.takeDamage(dmg + target.getEffectiveDefense());
        return dmg;
    }

    public void resetHunger() { turnsWithoutEating = 0; }
    public int getTurnsWithoutEating() { return turnsWithoutEating; }
}