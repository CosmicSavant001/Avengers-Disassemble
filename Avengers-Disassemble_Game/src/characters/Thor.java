package characters;

import abilities.LightningStrike;

/**
 * Thor – The Asgardian God of Thunder.
 * High damage, Lightning Strike hits hard with an AoE-style shock effect.
 */
public class Thor extends Hero {

    public Thor() {
        super("Thor", 130, 25, 10);
        setSpecialAbility(new LightningStrike());
        setImagePath("assets/images/heroes/thor.png");
    }

    @Override
    public String getDescription() {
        return "Strongest Avenger? He would say so. " +
               "Thor channels the power of storms through Mjolnir, " +
               "and his Lightning Strike stuns enemies while dealing " +
               "devastating electrical damage.";
    }
}
