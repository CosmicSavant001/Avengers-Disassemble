package characters;

public class Enemy extends Character {

    private String type;
    private int    xpReward;
    private int    scoreReward;

    public Enemy(String name, String type, int maxHealth, int attackPower,
                 int defensePower, int maxMana, int xpReward, int scoreReward) {
        super(name, maxHealth, attackPower, defensePower, maxMana);
        this.type        = type;
        this.xpReward    = xpReward;
        this.scoreReward = scoreReward;
        this.imagePath   = "assets/images/enemies/"
                + name.toLowerCase().replace(" ", "_") + ".png";
    }

    public static Enemy fromHero(Hero hero) {
        Enemy e = new Enemy(
                hero.getName() + " (Clone)", "Clone",
                hero.getMaxHealth(), hero.getAttackPower(),
                hero.getDefensePower(), hero.getMaxMana(),
                80, 200
        );
        e.setImagePath(hero.getImagePath());
        e.setSpecialAbility(hero.getSpecialAbility());
        return e;
    }

    public String getType()        { return type; }
    public int    getXpReward()    { return xpReward; }
    public int    getScoreReward() { return scoreReward; }
}