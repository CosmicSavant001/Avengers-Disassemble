package characters;

/**
 * Abstract base class for all playable hero characters.
 * Heroes are controlled by the player and have named special abilities.
 */
public abstract class Hero extends Character {

    private int level;
    private int experience;
    private static final int XP_PER_LEVEL = 100;

    public Hero(String name, int maxHealth, int attackPower, int defensePower) {
        super(name, maxHealth, attackPower, defensePower);
        this.level = 1;
        this.experience = 0;
    }

    /**
     * Grants experience points and handles level-up logic.
     * @param xp amount of XP earned
     */
    public void gainExperience(int xp) {
        this.experience += xp;
        while (this.experience >= XP_PER_LEVEL) {
            this.experience -= XP_PER_LEVEL;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        // Improve stats on level-up
        maxHealth     += 10;
        currentHealth  = maxHealth; // full heal on level-up
        attackPower   += 2;
        defensePower  += 1;
        System.out.println(name + " leveled up to level " + level + "!");
    }

    // ─── Abstract methods each hero must implement ────────────────────────────

    /** Brief lore / backstory shown on the character select screen. */
    public abstract String getDescription();

    // ─── Getters ─────────────────────────────────────────────────────────────

    public int getLevel()      { return level; }
    public int getExperience() { return experience; }
}
