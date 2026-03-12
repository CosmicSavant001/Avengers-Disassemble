package game;

import java.io.*;
import java.util.*;

/**
 * Manages the persistent leaderboard.
 * Stores: heroName, score, mode, date
 * No login required — scores are saved by hero name + mode.
 */
public class Leaderboard {

    private static final String DATA_DIR   = "data/";
    private static final String SCORE_FILE = DATA_DIR + "leaderboard.dat";
    private static final int    MAX_ENTRIES = 10;

    public static class Entry implements Comparable<Entry> {
        private final String username; // kept as "heroName" for display
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

        @Override public int compareTo(Entry o) { return Integer.compare(o.score, this.score); }
    }

    private List<Entry> entries = new ArrayList<>();

    public Leaderboard() {
        ensureDataDirectory();
        load();
    }

    public void submitScore(String heroName, int score, String mode) {
        String date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date());
        entries.add(new Entry(heroName, score, mode, date));
        Collections.sort(entries);
        if (entries.size() > MAX_ENTRIES) entries = entries.subList(0, MAX_ENTRIES);
        save();
    }

    public List<Entry> getTopScores() { return Collections.unmodifiableList(entries); }

    public int getBestScore(String heroName) {
        return entries.stream()
                .filter(e -> e.getUsername().equalsIgnoreCase(heroName))
                .mapToInt(Entry::getScore).max().orElse(0);
    }

    private void load() {
        File f = new File(SCORE_FILE);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",", 4);
                if (p.length == 4) {
                    try { entries.add(new Entry(p[0].trim(),
                            Integer.parseInt(p[1].trim()), p[2].trim(), p[3].trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }
            Collections.sort(entries);
        } catch (IOException e) { System.err.println("Leaderboard load error: " + e.getMessage()); }
    }

    private void save() {
        try (PrintWriter w = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (Entry e : entries)
                w.printf("%s,%d,%s,%s%n", e.getUsername(), e.getScore(), e.getHeroName(), e.getDate());
        } catch (IOException e) { System.err.println("Leaderboard save error: " + e.getMessage()); }
    }

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }
}
