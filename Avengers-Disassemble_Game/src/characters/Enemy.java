package characters;

/**
 * Represents an AI-controlled enemy character.
 * Enemies are managed by the EnemyAI and do not have level/XP systems.
 */
public class Enemy extends Character {

    private String type;        // e.g., "Thanos", "Loki", "Ultron"
    private int xpReward;       // XP granted to the hero on defeat
    private int scoreReward;    // Score points granted on defeat
    private int difficultyTier; // 1 = easy, 2 = medium, 3 = hard

    public Enemy(String name, String type, int maxHealth,
                 int attackPower, int defensePower,
                 int xpReward, int scoreReward, int difficultyTier) {
        super(name, maxHealth, attackPower, defensePower);
        this.type = type;
        this.xpReward = xpReward;
        this.scoreReward = scoreReward;
        this.difficultyTier = difficultyTier;
        this.imagePath = "assets/images/enemies/" + name.toLowerCase().replace(" ", "_") + ".png";
    }

    // ─── Factory Methods ──────────────────────────────────────────────────────

    /** Creates the iconic boss enemy – Thanos */
    public static Enemy createThanos() {
        Enemy e = new Enemy("Thanos", "Cosmic Titan",
                200, 25, 12, 150, 500, 3);
        e.setImagePath("assets/images/enemies/thanos.png");
        return e;
    }

    /** Creates the trickster god – Loki */
    public static Enemy createLoki() {
        Enemy e = new Enemy("Loki", "Asgardian Trickster",
                130, 18, 8, 100, 300, 2);
        e.setImagePath("assets/images/enemies/loki.png");
        return e;
    }

    /** Creates the robotic villain – Ultron */
    public static Enemy createUltron() {
        Enemy e = new Enemy("Ultron", "Rogue AI",
                150, 22, 10, 120, 400, 2);
        e.setImagePath("assets/images/enemies/ultron.png");
        return e;
    }

    /** Creates a basic henchman for easy fights */
    public static Enemy createHydraAgent() {
        Enemy e = new Enemy("HYDRA Agent", "Henchman",
                80, 12, 4, 50, 100, 1);
        e.setImagePath("assets/images/enemies/hydra_agent.png");
        return e;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getType()        { return type; }
    public int getXpReward()       { return xpReward; }
    public int getScoreReward()    { return scoreReward; }
    public int getDifficultyTier() { return difficultyTier; }
}
