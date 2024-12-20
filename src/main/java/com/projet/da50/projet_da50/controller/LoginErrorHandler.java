package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class LoginErrorHandler {

    private UserController userController;
    private Map<String, LoginAttempt> loginAttempts; // Map pour stocker les tentatives de connexion
    private static final long LOCK_TIME_DURATION = 60 * 1000; // 30 secondes en millisecondes

    public LoginErrorHandler() {
        this.userController = new UserController();
        this.loginAttempts = new HashMap<>();
    }

    public String validateAuthenticationFields(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return "Please fill in all fields.";
        }

        User user = userController.findUserByUsername(username);

        if (user == null) {
            return "Invalid username or password.";
        }

        // Récupérer ou créer l'objet LoginAttempt pour cet utilisateur
        LoginAttempt attempt = loginAttempts.computeIfAbsent(username, k -> new LoginAttempt());

        if (attempt.isLocked(LOCK_TIME_DURATION)) {
            long remainingTime = ChronoUnit.MILLIS.between(LocalDateTime.now(), attempt.getLastAttemptTime().plus(LOCK_TIME_DURATION, ChronoUnit.MILLIS));
            return "Account locked. Try again in " + remainingTime / 1000 + " seconds.";
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            attempt.resetAttempts();
            loginAttempts.remove(username); // Supprimer l'entrée de la map après une connexion réussie
            return "Valid credentials.";
        } else {
            attempt.incrementAttempts();
            return "Invalid username or password.";
        }
    }

    public String validateCreateAccountFields(String username, String password, String mail) {
        if (username.isEmpty() || password.isEmpty() || mail.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (password.length() < 6) {
            return "Password should be at least 6 characters long.";
        }
        if (!mail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return "Mail format is invalid.";
        }
        if (userController.findUserByUsername(username)!=null) {
            return "This username is already taken.";
        }
        if (userController.findUserByMail(mail)!=null) {
            return "This mail is already used.";
        }
        return "Valid credentials.";
    }

    public String validateForgotPasswordFields(String mail) {
        if (mail.isEmpty()) {
            return "Please fill in all fields.";
        }
        if (!mail.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            return "Mail format is invalid.";
        }
        if (userController.findUserByMail(mail) == null) {
            return "This mail does not exist";
        }
        //If everything is ok:
        return "Valid credentials.";
    }
}