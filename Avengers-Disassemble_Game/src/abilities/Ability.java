package abilities;

import characters.Character;

/**
 * Abstract base class for all special abilities.
 * Abilities have a cooldown system and a defined activation effect.
 */
public abstract class Ability {

    protected String name;
    protected String description;
    protected int baseDamage;
    protected int cooldownMax;    // turns before ability can be used again
    protected int cooldownRemaining;
    protected String soundFile;   // placeholder for audio effect

    public Ability(String name, String description, int baseDamage, int cooldownMax) {
        this.name = name;
        this.description = description;
        this.baseDamage = baseDamage;
        this.cooldownMax = cooldownMax;
        this.cooldownRemaining = 0; // available at the start
        this.soundFile = "assets/audio/" + name.toLowerCase().replace(" ", "_") + ".wav";
    }

    // ─── Core Methods ─────────────────────────────────────────────────────────

    /**
     * Activates the ability. Subclasses define the actual effect.
     * @param user   the character using the ability
     * @param target the target of the ability
     * @return value representing damage dealt or healing done
     */
    public int activate(Character user, Character target) {
        if (!isReady()) {
            System.out.println(name + " is on cooldown! (" + cooldownRemaining + " turns left)");
            return 0;
        }
        cooldownRemaining = cooldownMax;
        return applyEffect(user, target);
    }

    /**
     * Called each turn to reduce cooldown counter.
     */
    public void tickCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    /**
     * The specific effect of this ability. Must be implemented by subclasses.
     */
    protected abstract int applyEffect(Character user, Character target);

    // ─── Utility ──────────────────────────────────────────────────────────────

    public boolean isReady() {
        return cooldownRemaining == 0;
    }

    public String getStatusText() {
        return isReady() ? "READY" : "CD: " + cooldownRemaining;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public int getBaseDamage()           { return baseDamage; }
    public int getCooldownMax()          { return cooldownMax; }
    public int getCooldownRemaining()    { return cooldownRemaining; }
    public String getSoundFile()         { return soundFile; }

    @Override
    public String toString() {
        return name + " [" + getStatusText() + "] – " + description;
    }
}
