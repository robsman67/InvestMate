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

public class TokenManager {

    private static final String TOKEN_FILE;
    private static final long TOKEN_VALIDITY_DURATION = 24 * 60 * 60; // 24 heures en secondes
    private static final Map<String, String> activeTokens = new HashMap<>(); // Stockage des tokens actifs en mémoire
    private static final String TOKEN_SEPARATOR = ";"; // Séparateur modifié
    private static final UserController userController = new UserController();
    private static final LogController logController = new LogController();
    public static boolean stayLogged = false;



    static {
        // Déterminer l'emplacement du fichier spécifique à la plateforme
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;
        if (os.contains("mac")) {
            baseDir = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "MyApp";
        } else {
            baseDir = System.getProperty("user.home");
        }

        // S'assurer que le répertoire existe
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

    // Générer un jeton pour l'utilisateur donné
    public static String generateToken(String username) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        // Combiner le token avec le nom d'utilisateur
        String combinedToken = token + TOKEN_SEPARATOR + username;

        // Ajouter le token à la liste des tokens actifs
        activeTokens.put(username, combinedToken);

        return combinedToken;
    }

    // Enregistrer le jeton dans un fichier avec une validité de 24 heures si l'option "stayLoggedIn" est activée
    public static void saveToken(String token, boolean stayLoggedIn) {
        if (!stayLoggedIn) {
            System.out.println("Token will not be saved for non-stay logged in sessions.");
            return;
        }
        try {
            // Ajouter l'horodatage actuel au jeton
            String tokenWithTimestamp = token + TOKEN_SEPARATOR + Instant.now().atZone(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

            Files.write(Paths.get(TOKEN_FILE), tokenWithTimestamp.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Token saved successfully.");
            stayLogged = true;
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
        }
    }

    // Supprimer le jeton de la mémoire et du fichier
    public static void deleteToken() {
        // Supprimer le token de la liste des tokens actifs
        activeTokens.clear();

        // Supprimer le fichier de token
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

    public static String extractUsernameFromToken(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        String[] parts = token.split(TOKEN_SEPARATOR);
        if (parts.length == 2) {
            // Le nom d'utilisateur est la deuxième partie
            return parts[1];
        } else {
            System.err.println("Invalid token format for username extraction.");
            return null;
        }
    }


    //For the log
    public static long getIdToken(){
        String token = getToken();
        String username = extractUsernameFromToken(token);
        if(username != null){
            User user = userController.findUserByUsername(username);
            return user.getId();
        }
        else {
            return (long) -1;
        }
    }

    // Vérifier si le jeton est valide
    public static boolean isTokenValid(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        String username = extractUsernameFromToken(token);
        if (username == null) {
            return false;
        }

        // Vérifier si le token est dans la liste des tokens actifs
        if (!activeTokens.containsKey(username)) {
            // Si le token n'est pas dans la liste des tokens actifs, vérifier s'il est valide dans le fichier
            try {
                if (Files.exists(Paths.get(TOKEN_FILE))) {
                    String tokenWithTimestamp = Files.readString(Paths.get(TOKEN_FILE));
                    String[] parts = tokenWithTimestamp.split(TOKEN_SEPARATOR);

                    String storedToken = "";
                    Instant timestamp = null;

                    if (parts.length == 3) {
                        storedToken = parts[0] + TOKEN_SEPARATOR + parts[1]; // Reconstruire le token stocké sans l'horodatage
                        timestamp = Instant.parse(parts[2]);
                    } else if(parts.length == 2){
                        storedToken = parts[0] + TOKEN_SEPARATOR + parts[1];
                    } else {
                        System.err.println("Invalid token format.");
                        return false;
                    }


                    // Vérifier si le token est correct et s'il n'a pas expiré
                    if (token.equals(storedToken) &&
                            (parts.length == 2 || timestamp.isAfter(Instant.now().minus(TOKEN_VALIDITY_DURATION, ChronoUnit.SECONDS)))) {
                        // Mettre à jour le token actif avec la nouvelle date d'expiration
                        activeTokens.put(username, token);
                        return true;
                    } else {
                        System.out.println("Token expired or invalid. Deleting...");
                        deleteToken(); // Supprimer le token expiré ou invalide
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
            // Si le token est dans la liste des tokens actifs, vérifier sa validité
            String activeToken = activeTokens.get(username);
            if (!token.equals(activeToken)) {
                return false;
            }
        }
        stayLogged = true;
        return true;
    }

    // Récupérer le jeton à partir d'un fichier
    public static String getToken() {
        // Chercher d'abord dans les tokens actifs en mémoire
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

        // Si aucun token actif n'est trouvé, essayer de récupérer le token à partir du fichier
        try {
            if (Files.exists(Paths.get(TOKEN_FILE))) {
                String tokenWithTimestamp = Files.readString(Paths.get(TOKEN_FILE));
                String[] parts = tokenWithTimestamp.split(TOKEN_SEPARATOR);
                if (parts.length == 3 || parts.length == 2) {
                    // Retourner le token complet (token + username)
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


    public static void shutDownDeleteToken(){
        // Delete token when we quit the app
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("App is shutting down." + stayLogged);
            if(!stayLogged) {
                logController.createLog(getIdToken(), "Logout by shutdown", "");
                System.out.println("App is shutting down. non persistent token deleted.");
            }
        }));

    }
}