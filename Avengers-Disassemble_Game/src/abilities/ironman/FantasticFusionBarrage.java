package abilities.ironman;
import abilities.Ability;
import characters.Character;
/** Skill 3 - Fantastic Fusion Barrage | Damage: 350-520 (All Enemies) */
public class FantasticFusionBarrage extends Ability {
    public FantasticFusionBarrage() {
        super("Fantastic Fusion Barrage", "ULTIMATE! Full power repulsor barrage. Damage: 350-520!", 350, 4, 200);
    }
    @Override protected int applyEffect(Character user, Character target) {
        int damage = 350 + (int)(Math.random() * 171);
        target.takeDamage(damage);
        System.out.println("FANTASTIC FUSION BARRAGE! " + damage + " MASSIVE DAMAGE!");
        return damage;
    }
}
