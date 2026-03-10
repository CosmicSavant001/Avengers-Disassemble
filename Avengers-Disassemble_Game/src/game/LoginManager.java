package game;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user registration and login.
 * Credentials are stored in a plain-text file (username:hashedPassword) per line.
 * Passwords are hashed with SHA-256 for basic security.
 */
public class LoginManager {

    private static final String DATA_DIR  = "data/";
    private static final String USER_FILE = DATA_DIR + "users.dat";

    private Map<String, String> userDatabase; // username -> hashedPassword

    public LoginManager() {
        userDatabase = new HashMap<>();
        ensureDataDirectory();
        loadUsers();
    }

    // ─── Public API ──────────────────────────────────────────────────────────

    /**
     * Attempts to log in with the given credentials.
     * @return true if credentials are valid
     */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) return false;
        String hashed = hash(password);
        return hashed.equals(userDatabase.get(username.toLowerCase()));
    }

    /**
     * Registers a new user account.
     * @return true if registration succeeded, false if username is taken
     */
    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return false;
        }
        String key = username.toLowerCase();
        if (userDatabase.containsKey(key)) return false; // already exists

        userDatabase.put(key, hash(password));
        saveUsers();
        return true;
    }

    /**
     * Returns true if a username is already registered.
     */
    public boolean userExists(String username) {
        return userDatabase.containsKey(username.toLowerCase());
    }

    // ─── File I/O ─────────────────────────────────────────────────────────────

    private void loadUsers() {
        File file = new File(USER_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    userDatabase.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not read user file – " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_FILE))) {
            for (Map.Entry<String, String> entry : userDatabase.entrySet()) {
                writer.println(entry.getKey() + ":" + entry.getValue());
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not save user file – " + e.getMessage());
        }
    }

    private void ensureDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    // ─── Hashing ──────────────────────────────────────────────────────────────

    private String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback (not secure, but avoids crash)
            return Integer.toHexString(input.hashCode());
        }
    }
}
