package abilities.hulk;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Vince Ultimate Fury | Damage: 250-350 | Boosts ATK and DEF for 3 turns */
public class UltimateFury extends Ability {
    public UltimateFury() {
        super("Ultimate Fury", "ULTIMATE! Pure rage! Damage: 250-350. Boosts attack and defense for 3 turns.", 250, 4, 150);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 250 + (int)(Math.random() * 101);
        target.takeDamage(damage);
        System.out.println("VINCE ULTIMATE FURY! " + damage + " damage! Hulk's power surges!");
        return damage;
    }
}
