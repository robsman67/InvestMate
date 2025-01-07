package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.model.User;

import java.io.*;
import java.nio.file.*;
import java.util.Base64;

public class TokenNotStayedLogin {
    private static final String TOKEN_FILE;
    private static final UserController userController = new UserController();

    // Static block to determine platform-specific file location
    static {
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (os.contains("mac")) {
            baseDir = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "MyApp";
        }else {
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

    // Save token to a file
    public static void saveToken(String token) {
        try {
            // Encode token to Base64 for basic obfuscation
            String encodedToken = Base64.getEncoder().encodeToString(token.getBytes());
            Files.write(Paths.get(TOKEN_FILE), encodedToken.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("Token saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
        }
    }

    // Retrieve token from a file
    public static String getToken() {
        try {
            if (Files.exists(Paths.get(TOKEN_FILE))) {
                String encodedToken = Files.readString(Paths.get(TOKEN_FILE));
                return new String(Base64.getDecoder().decode(encodedToken));
            } else {
                System.out.println("No token found.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error retrieving token: " + e.getMessage());
            return null;
        }
    }

    //For the log
    public static Long getIdToken(){
        String username = getToken();
        if(username != null){
            User user = userController.findUserByUsername(username);
            return user.getId();
        }
        else {
            return (long) -1;
        }
    }

    // Delete the token file
    public static void deleteToken() {
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

    public static void shutDownDeleteToken(){
        // Delete token when we quit the app
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            deleteToken();
            System.out.println("App is shutting down. Token deleted.");
        }));

    }

}
