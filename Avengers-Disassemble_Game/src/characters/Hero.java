package characters;

import abilities.Ability;

public abstract class Hero extends Character {

    private int level      = 1;
    private int experience = 0;
    private static final int XP_PER_LEVEL = 100;

    private Ability skill1;
    private Ability skill2;
    private Ability skill3;

    public Hero(String name, int maxHealth, int attackPower,
                int defensePower, int maxMana) {
        super(name, maxHealth, attackPower, defensePower, maxMana);
    }

    public void gainExperience(int xp) {
        this.experience += xp;
        while (this.experience >= XP_PER_LEVEL) {
            this.experience -= XP_PER_LEVEL;
            levelUp();
        }
    }

    private void levelUp() {
        level++;
        maxHealth    += 100;
        currentHealth = maxHealth;
        attackPower  += 5;
        defensePower += 2;
        System.out.println(name + " leveled up to level " + level + "!");
    }

    public void tickAllCooldowns() {
        if (skill1 != null) skill1.tickCooldown();
        if (skill2 != null) skill2.tickCooldown();
        if (skill3 != null) skill3.tickCooldown();
    }

    public int useSkill1(Character target) { return skill1 != null ? skill1.activate(this, target) : 0; }
    public int useSkill2(Character target) { return skill2 != null ? skill2.activate(this, target) : 0; }
    public int useSkill3(Character target) { return skill3 != null ? skill3.activate(this, target) : 0; }

    public Ability getSkill1() { return skill1; }
    public Ability getSkill2() { return skill2; }
    public Ability getSkill3() { return skill3; }
    public void setSkill1(Ability a) { this.skill1 = a; }
    public void setSkill2(Ability a) { this.skill2 = a; }
    public void setSkill3(Ability a) { this.skill3 = a; }

    public abstract String getDescription();

    public int getLevel()      { return level; }
    public int getExperience() { return experience; }
}