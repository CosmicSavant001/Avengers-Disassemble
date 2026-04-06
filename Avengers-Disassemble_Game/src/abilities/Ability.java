package abilities;

import characters.Character;

/**
 * Abstract base class for all special abilities.
 */
public abstract class Ability {

    protected String name;
    protected String description;
    protected int    baseDamage;
    protected int    cooldownMax;
    protected int    cooldownRemaining;
    protected int    manaCost;
    protected String soundFile;

    public Ability(String name, String description,
                   int baseDamage, int cooldownMax, int manaCost) {
        this.name              = name;
        this.description       = description;
        this.baseDamage        = baseDamage;
        this.cooldownMax       = cooldownMax;
        this.cooldownRemaining = 0;
        this.manaCost          = manaCost;
        this.soundFile         = "assets/audio/"
                + name.toLowerCase().replace(" ", "_") + ".wav";
    }

    // ── Core ──────────────────────────────────────────────────────────────

    public int activate(Character user, Character target) {
        if (!isReady()) {
            System.out.println(name + " is on cooldown! ("
                    + cooldownRemaining + " turns left)");
            return 0;
        }
        if (user.getCurrentMana() < manaCost) {
            System.out.println("Not enough mana for " + name + "!");
            return 0;
        }
        user.useMana(manaCost);
        cooldownRemaining = cooldownMax;
        return applyEffect(user, target);
    }

    public void tickCooldown() {
        if (cooldownRemaining > 0) cooldownRemaining--;
    }

    public void resetCooldown() {
        cooldownRemaining = 0;
    }

    protected abstract int applyEffect(Character user, Character target);

    // ── Helpers ───────────────────────────────────────────────────────────

    public boolean isReady() { return cooldownRemaining == 0; }

    public String getStatusText() {
        return isReady() ? "READY" : "CD:" + cooldownRemaining;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public String getName()           { return name; }
    public String getDescription()    { return description; }
    public int    getBaseDamage()     { return baseDamage; }
    public int    getCooldownMax()    { return cooldownMax; }
    public int    getCooldownRemaining() { return cooldownRemaining; }
    public int    getManaCost()       { return manaCost; }
    public String getSoundFile()      { return soundFile; }

    @Override
    public String toString() {
        return name + " [" + getStatusText() + "] – " + description;
    }
}
