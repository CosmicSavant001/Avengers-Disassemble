package characters;

import abilities.RepulsorBlast;

/**
 * Iron Man – Tony Stark in the Mark L armor.
 * High attack power, moderate defense, iconic Repulsor Blast ability.
 */
public class IronMan extends Hero {

    public IronMan() {
        super("Iron Man", 120, 22, 8);
        setSpecialAbility(new RepulsorBlast());
        setImagePath("assets/images/heroes/ironman.png");
    }

    @Override
    public String getDescription() {
        return "Genius, billionaire, playboy, philanthropist. " +
               "Tony Stark's cutting-edge armor deals massive energy damage " +
               "with the iconic Repulsor Blast.";
    }
}
