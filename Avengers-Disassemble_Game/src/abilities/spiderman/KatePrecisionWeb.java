package abilities.spiderman;

import abilities.Ability;
import characters.Character;

/** Skill 1 – Kate Precision Web | Damage: 130-210 | Effect: Immobilize 1 turn */
public class KatePrecisionWeb extends Ability {

    private boolean webbed     = false;
    private Character webbedTarget = null;

    public KatePrecisionWeb() {
        super("Kate Precision Web",
              "Shoots a precision web shot. Damage: 130-210. Immobilizes for 1 turn.",
              130, 2, 70);
    }

    @Override
    protected int applyEffect(Character user, Character target) {
        int damage = 130 + (int)(Math.random() * 81); // 130-210
        target.takeDamage(damage);
        webbed       = true;
        webbedTarget = target;
        System.out.println(user.getName() + " webs " + target.getName()
                + " for " + damage + "! Target is IMMOBILIZED!");
        return damage;
    }

    public boolean isTargetWebbed()  { return webbed; }
    public void    clearWeb()        { webbed = false; webbedTarget = null; }
}
