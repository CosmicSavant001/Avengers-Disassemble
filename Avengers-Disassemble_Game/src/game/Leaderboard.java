package game;

import java.io.*;
import java.util.*;

/**
 * Manages the persistent leaderboard.
 * Scores are stored in CSV format: username,score,heroName,date
 * Top 10 scores are displayed in the UI.
 */
public class Leaderboard {

    private static final String DATA_DIR  = "data/";
    private static final String SCORE_FILE = DATA_DIR + "leaderboard.dat";
    private static final int MAX_ENTRIES   = 10;

    /** Represents a single leaderboard entry. */
    public static class Entry implements Comparable<Entry> {
        private final String username;
        private final int    score;
        private final String heroName;
        private final String date;

        public Entry(String username, int score, String heroName, String date) {
            this.username = username;
            this.score    = score;
            this.heroName = heroName;
            this.date     = date;
        }

        public String getUsername() { return username; }
        public int    getScore()    { return score; }
        public String getHeroName() { return heroName; }
        public String getDate()     { return date; }

        @Override
        public int compareTo(Entry other) {
            return Integer.compare(other.score, this.score); // descending
        }

        @Override
        public String toString() {
            return String.format("%-15s %-12s %6d  %s",
                    username, heroName, score, date);
        }
    }

    private List<Entry> entries;

    public Leaderboard() {
        entries = new ArrayList<>();
        ensureDataDirectory();
        load();
    }

    // ─── Public API ──────────────────────────────────────────────────────────

    /**
     * Submits a new score. The leaderboard is sorted and trimmed to MAX_ENTRIES.
     */
    public void submitScore(String username, int score, String heroName) {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        entries.add(new Entry(username, score, heroName, date));
        Collections.sort(entries);
        if (entries.size() > MAX_ENTRIES) {
            entries = entries.subList(0, MAX_ENTRIES);
        }
        save();
    }

    /**
     * Returns an immutable view of the top scores.
     */
    public List<Entry> getTopScores() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Returns the best score for a specific user, or 0 if none.
     */
    public int getBestScore(String username) {
        return entries.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(username))
                .mapToInt(Entry::getScore)
                .max()
                .orElse(0);
    }

    // ─── File I/O ─────────────────────────────────────────────────────────────

    private void load() {
        File file = new File(SCORE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length == 4) {
                    try {
                        entries.add(new Entry(
                                parts[0].trim(),
                                Integer.parseInt(parts[1].trim()),
                                parts[2].trim(),
                                parts[3].trim()
                        ));
                    } catch (NumberFormatException ignored) {}
                }
            }
            Collections.sort(entries);
        } catch (IOException e) {
            System.err.println("Warning: Could not read leaderboard – " + e.getMessage());
        }
    }

    private void save() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (Entry e : entries) {
                writer.printf("%s,%d,%s,%s%n", e.getUsername(), e.getScore(), e.getHeroName(), e.getDate());
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not save leaderboard – " + e.getMessage());
        }
    }

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }
}
