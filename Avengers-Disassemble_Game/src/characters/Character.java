package characters;

import abilities.Ability;

/**
 * Abstract base class representing any character in the game.
 * All heroes and enemies extend this class.
 */
public abstract class Character {

    protected String name;
    protected int maxHealth;
    protected int currentHealth;
    protected int attackPower;
    protected int defensePower;
    protected Ability specialAbility;
    protected boolean isDefending;
    protected String imagePath; // placeholder for character portrait

    public Character(String name, int maxHealth, int attackPower, int defensePower) {
        this.name = name;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.attackPower = attackPower;
        this.defensePower = defensePower;
        this.isDefending = false;
        this.imagePath = "assets/images/placeholder.png";
    }

    // ─── Core Actions ────────────────────────────────────────────────────────

    /**
     * Performs a basic attack on the target character.
     * @return damage dealt
     */
    public int attack(Character target) {
        int damage = Math.max(1, this.attackPower - target.getEffectiveDefense());
        target.takeDamage(damage);
        return damage;
    }

    /**
     * Uses the character's special ability on the target.
     * @return damage or effect value
     */
    public int useAbility(Character target) {
        if (specialAbility != null && specialAbility.isReady()) {
            return specialAbility.activate(this, target);
        }
        return 0;
    }

    /**
     * Puts the character into a defensive stance for this turn.
     */
    public void defend() {
        this.isDefending = true;
    }

    /**
     * Resets the defending state at the start of a new turn.
     */
    public void resetDefend() {
        this.isDefending = false;
    }

    /**
     * Applies damage to this character, accounting for defense.
     * @param rawDamage incoming damage before mitigation
     */
    public void takeDamage(int rawDamage) {
        int effective = Math.max(1, rawDamage - getEffectiveDefense());
        currentHealth = Math.max(0, currentHealth - effective);
    }

    /**
     * Heals the character by the given amount, capped at max health.
     */
    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    // ─── Computed Properties ─────────────────────────────────────────────────

    public int getEffectiveDefense() {
        return isDefending ? defensePower * 2 : defensePower;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public double getHealthPercentage() {
        return (double) currentHealth / maxHealth;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public String getName()            { return name; }
    public int getMaxHealth()          { return maxHealth; }
    public int getCurrentHealth()      { return currentHealth; }
    public int getAttackPower()        { return attackPower; }
    public int getDefensePower()       { return defensePower; }
    public Ability getSpecialAbility() { return specialAbility; }
    public boolean isDefending()       { return isDefending; }
    public String getImagePath()       { return imagePath; }

    public void setSpecialAbility(Ability ability) { this.specialAbility = ability; }
    public void setImagePath(String path)          { this.imagePath = path; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d]",
                name, currentHealth, maxHealth, attackPower, defensePower);
    }
}
