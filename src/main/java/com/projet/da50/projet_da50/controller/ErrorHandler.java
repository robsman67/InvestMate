package com.projet.da50.projet_da50.controller;

import com.projet.da50.projet_da50.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class ErrorHandler {

    private UserController userController;
    private Map<String, LoginAttempt> loginAttempts;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 60 * 1000; // 60 secondes en millisecondes

    public ErrorHandler() {
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

        LoginAttempt attempt = loginAttempts.getOrDefault(username, new LoginAttempt());

        if (attempt.isLocked(LOCK_TIME_DURATION)) {
            long remainingTime = ChronoUnit.MILLIS.between(LocalDateTime.now(), attempt.getLastAttemptTime().plus(LOCK_TIME_DURATION, ChronoUnit.MILLIS));
            return "Account locked. Try again in " + remainingTime / 1000 + " seconds.";
        }

        if (BCrypt.checkpw(password, user.getPassword())) {
            attempt.resetAttempts();
            loginAttempts.remove(username);
//            if (user.getRole() != Role.Admin) {
//                String token = TokenManager.generateToken(username);
//                return "Valid credentials." + token;
//            } else {
//                return "Valid credentials.";
//            }
            return "Valid credentials.";
        } else {
            attempt.incrementAttempts();
            loginAttempts.put(username, attempt);
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
        if (!isValidEmail(mail)) {
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
        if (!isValidEmail(mail)) {
            return "Mail format is invalid.";
        }
        if (userController.findUserByMail(mail) == null) {
            return "This mail does not exist";
        }
        //If everything is ok:
        return "Valid credentials.";
    }

    //  Valider le format du mail
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    //  Vérifier si le mail est déjà utilisé
    public boolean isEmailAlreadyUsed(String email) {
        return userController.findUserByMail(email) != null;
    }

    //  Vérifier si le username est déjà utilisé
    public boolean isUsernameAlreadyTaken(String username) {
        return userController.findUserByUsername(username) != null;
    }

    //  Vérifier si le mot de passe a au moins 6 caractères
    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    // Vérifier si le mot de passe actuel est correct
    public boolean isCurrentPasswordCorrect(User user, String currentPassword) {
        return BCrypt.checkpw(currentPassword, user.getPassword());
    }
}