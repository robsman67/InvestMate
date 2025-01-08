package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.model.User;

import java.io.*;
import java.nio.file.*;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * This class manages the generation, validation, and storage of tokens for user authentication.
 */
public class TokenManager {

    private static final String TOKEN_FILE;
    private static final long TOKEN_VALIDITY_DURATION = 24 * 60 * 60; // 24 hours in seconds
    private static final Map<String, String> activeTokens = new HashMap<>(); // Storage of active tokens in memory
    private static final String TOKEN_SEPARATOR = ";"; // Modified separator
    private static final UserController userController = new UserController();
    private static final LogController logController = new LogController();
    public static boolean stayLogged = false;

    static {
        // Determine the platform-specific file location
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;
        if (os.contains("mac")) {
            baseDir = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "MyApp";
        } else {
            baseDir = System.getProperty("user.home");
        }

        // Ensure the directory exists
        Path directoryPath = Paths.get(baseDir);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                System.err.println("Error creating directory: " + e.getMessage());
            }
        }
        TOKEN_FILE = baseDir + File.separator + ".myapp_token";
    }

    /**
     * Generates a token for the given user.
     *
     * @param username The username for which the token is generated.
     * @return The generated token.
     */
    public static String generateToken(String username) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        // Combine the token with the username
        String combinedToken = token + TOKEN_SEPARATOR + username;

        // Add the token to the list of active tokens
        activeTokens.put(username, combinedToken);

        return combinedToken;
    }

    /**
     * Saves the token to a file with a validity of 24 hours if the "stayLoggedIn" option is enabled.
     *
     * @param token The token to be saved.
     * @param stayLoggedIn Whether the user chose to stay logged in.
     */
    public static void saveToken(String token, boolean stayLoggedIn) {
        if (!stayLoggedIn) {
            System.out.println("Token will not be saved for non-stay logged in sessions.");
            return;
        }
        try {
            // Add the current timestamp to the token
            String tokenWithTimestamp = token + TOKEN_SEPARATOR + Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            Files.write(Paths.get(TOKEN_FILE), tokenWithTimestamp.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Token saved successfully.");
            stayLogged = true;
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
        }
    }

    /**
     * Deletes the token from memory and from the file.
     */
    public static void deleteToken() {
        // Remove the token from the list of active tokens
        activeTokens.clear();

        // Delete the token file
        try {
            Path path = Paths.get(TOKEN_FILE);
            if (Files.exists(path)) {
                Files.delete(path);
                System.out.println("Token deleted successfully.");
            } else {
                System.out.println("No token to delete.");
            }
        } catch (IOException e) {
            System.err.println("Error deleting token: " + e.getMessage());
        }
    }

    /**
     * Extracts the username from the given token.
     *
     * @param token The token from which the username is extracted.
     * @return The extracted username, or null if the token is invalid.
     */
    public static String extractUsernameFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        String[] parts = token.split(TOKEN_SEPARATOR);
        if (parts.length == 2) {
            // The username is the second part
            return parts[1];
        } else {
            System.err.println("Invalid token format for username extraction.");
            return null;
        }
    }

    /**
     * Retrieves the user ID associated with the current token.
     *
     * @return The user ID, or -1 if the token is invalid.
     */
    public static long getIdToken() {
        String token = getToken();
        String username = extractUsernameFromToken(token);
        if (username != null) {
            User user = userController.findUserByUsername(username);
            return user.getId();
        } else {
            return -1;
        }
    }

    /**
     * Checks if the given token is valid.
     *
     * @param token The token to be validated.
     * @return True if the token is valid, false otherwise.
     */
    public static boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        String username = extractUsernameFromToken(token);
        if (username == null) {
            return false;
        }

        // Check if the token is in the list of active tokens
        if (!activeTokens.containsKey(username)) {
            // If the token is not in the list of active tokens, check if it is valid in the file
            try {
                if (Files.exists(Paths.get(TOKEN_FILE))) {
                    String tokenWithTimestamp = Files.readString(Paths.get(TOKEN_FILE));
                    String[] parts = tokenWithTimestamp.split(TOKEN_SEPARATOR);

                    String storedToken = "";
                    Instant timestamp = null;

                    if (parts.length == 3) {
                        storedToken = parts[0] + TOKEN_SEPARATOR + parts[1]; // Reconstruct the stored token without the timestamp
                        timestamp = Instant.parse(parts[2]);
                    } else if (parts.length == 2) {
                        storedToken = parts[0] + TOKEN_SEPARATOR + parts[1];
                    } else {
                        System.err.println("Invalid token format.");
                        return false;
                    }

                    // Check if the token is correct and has not expired
                    if (token.equals(storedToken) &&
                            (parts.length == 2 || timestamp.isAfter(Instant.now().minus(TOKEN_VALIDITY_DURATION, ChronoUnit.SECONDS)))) {
                        // Update the active token with the new expiration date
                        activeTokens.put(username, token);
                        return true;
                    } else {
                        System.out.println("Token expired or invalid. Deleting...");
                        deleteToken(); // Delete the expired or invalid token
                        return false;
                    }
                } else {
                    System.out.println("No token found.");
                    return false;
                }
            } catch (IOException e) {
                System.err.println("Error retrieving token: " + e.getMessage());
                return false;
            }
        } else {
            // If the token is in the list of active tokens, check its validity
            String activeToken = activeTokens.get(username);
            if (!token.equals(activeToken)) {
                return false;
            }
        }
        stayLogged = true;
        return true;
    }

    /**
     * Retrieves the token from a file.
     *
     * @return The retrieved token, or null if no token is found.
     */
    public static String getToken() {
        // First, check in the active tokens in memory
        String usernameFromActiveToken = activeTokens.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);

        if (usernameFromActiveToken != null) {
            String activeToken = activeTokens.get(usernameFromActiveToken);
            if (activeToken != null && !activeToken.isEmpty()) {
                System.out.println("Token found in active tokens.");
                return activeToken;
            }
        }

        // If no active token is found, try to retrieve the token from the file
        try {
            if (Files.exists(Paths.get(TOKEN_FILE))) {
                String tokenWithTimestamp = Files.readString(Paths.get(TOKEN_FILE));
                String[] parts = tokenWithTimestamp.split(TOKEN_SEPARATOR);
                if (parts.length == 3 || parts.length == 2) {
                    // Return the complete token (token + username)
                    System.out.println("Token found in file.");
                    return parts[0] + TOKEN_SEPARATOR + parts[1];
                } else {
                    System.err.println("Invalid token format.");
                    return null;
                }
            } else {
                System.out.println("No token found in file.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error retrieving token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes the token when the application is closed.
     */
    public static void shutDownDeleteToken(long idToken) {
        // Delete token when we quit the app
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("App is shutting down." + stayLogged);
            if (!stayLogged) {
                logController.createLog(getIdToken(), "Logout by shutdown", "");
                System.out.println("App is shutting down. non persistent token deleted.");
            }
        }));
    }
}