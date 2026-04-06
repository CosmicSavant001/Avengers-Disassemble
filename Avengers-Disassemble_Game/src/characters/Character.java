package characters;

import abilities.Ability;

/**
 * Abstract base class for all characters in the game.
 * Now includes a Mana system alongside HP.
 */
public abstract class Character {

    protected String name;
    protected int    maxHealth;
    protected int    currentHealth;
    protected int    attackPower;
    protected int    defensePower;
    protected int    maxMana;
    protected int    currentMana;
    protected Ability specialAbility;
    protected boolean isDefending;
    protected String  imagePath;

    public Character(String name, int maxHealth, int attackPower,
                     int defensePower, int maxMana) {
        this.name          = name;
        this.maxHealth     = maxHealth;
        this.currentHealth = maxHealth;
        this.attackPower   = attackPower;
        this.defensePower  = defensePower;
        this.maxMana       = maxMana;
        this.currentMana   = maxMana;
        this.isDefending   = false;
        this.imagePath     = "assets/images/placeholder.png";
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public int attack(Character target) {
        int damage = Math.max(1, this.attackPower - target.getEffectiveDefense());
        target.takeDamage(damage);
        return damage;
    }

    public int useAbility(Character target) {
        if (specialAbility != null && specialAbility.isReady()) {
            return specialAbility.activate(this, target);
        }
        return 0;
    }

    public void defend() {
        this.isDefending = true;
    }

    public void resetDefend() {
        this.isDefending = false;
    }

    public void takeDamage(int rawDamage) {
        int effective = Math.max(1, rawDamage - getEffectiveDefense());
        currentHealth = Math.max(0, currentHealth - effective);
    }

    public void heal(int amount) {
        currentHealth = Math.min(maxHealth, currentHealth + amount);
    }

    public void restoreMana(int amount) {
        currentMana = Math.min(maxMana, currentMana + amount);
    }

    public void useMana(int amount) {
        currentMana = Math.max(0, currentMana - amount);
    }

    /** Full restore of HP and Mana — used between Arcade rounds */
    public void fullRestore() {
        currentHealth = maxHealth;
        currentMana   = maxMana;
        isDefending   = false;
        if (specialAbility != null) specialAbility.resetCooldown();
    }

    // ── Computed ──────────────────────────────────────────────────────────

    public int getEffectiveDefense() {
        return isDefending ? defensePower * 2 : defensePower;
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public double getHealthPercentage() {
        return (double) currentHealth / maxHealth;
    }

    public double getManaPercentage() {
        return (double) currentMana / maxMana;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public String   getName()             { return name; }
    public int      getMaxHealth()        { return maxHealth; }
    public int      getCurrentHealth()    { return currentHealth; }
    public int      getAttackPower()      { return attackPower; }
    public int      getDefensePower()     { return defensePower; }
    public int      getMaxMana()          { return maxMana; }
    public int      getCurrentMana()      { return currentMana; }
    public Ability  getSpecialAbility()   { return specialAbility; }
    public boolean  isDefending()         { return isDefending; }
    public String   getImagePath()        { return imagePath; }

    public void setSpecialAbility(Ability a) { this.specialAbility = a; }
    public void setImagePath(String path)    { this.imagePath = path; }

    @Override
    public String toString() {
        return String.format("%s [HP:%d/%d | MP:%d/%d | ATK:%d | DEF:%d]",
                name, currentHealth, maxHealth,
                currentMana, maxMana, attackPower, defensePower);
    }
}
