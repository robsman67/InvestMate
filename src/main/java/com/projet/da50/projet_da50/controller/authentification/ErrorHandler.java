package com.projet.da50.projet_da50.controller.authentification;

import com.projet.da50.projet_da50.controller.user.UserController;
import com.projet.da50.projet_da50.model.user.User;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handles error validation for user authentication and account creation.
 */
public class ErrorHandler {

    private final UserController userController;
    private final Map<String, LoginAttempt> loginAttempts;
    private static final long LOCK_TIME_DURATION = 60 * 1000; // 60 seconds in milliseconds

    /**
     * Constructor for ErrorHandler.
     * Initializes the UserController and loginAttempts map.
     */
    public ErrorHandler() {
        this.userController = new UserController();
        this.loginAttempts = new HashMap<>();
    }

    /**
     * Validates the authentication fields.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @return A validation message indicating the result of the validation.
     */
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
            return "Valid credentials.";
        } else {
            attempt.incrementAttempts();
            loginAttempts.put(username, attempt);
            return "Invalid username or password.";
        }
    }

    /**
     * Validates the fields for creating a new account.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param mail The email entered by the user.
     * @return A validation message indicating the result of the validation.
     */
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
        if (userController.findUserByUsername(username) != null) {
            return "This username is already taken.";
        }
        if (userController.findUserByMail(mail) != null) {
            return "This mail is already used.";
        }
        return "Valid credentials.";
    }

    /**
     * Validates the fields for resetting the password.
     *
     * @param mail The email entered by the user.
     * @return A validation message indicating the result of the validation.
     */
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
        return "Valid credentials.";
    }

    /**
     * Validates the format of the email.
     *
     * @param email The email to be validated.
     * @return True if the email format is valid, false otherwise.
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Checks if the email is already used.
     *
     * @param email The email to be checked.
     * @return True if the email is already used, false otherwise.
     */
    public boolean isEmailAlreadyUsed(String email) {
        return userController.findUserByMail(email) != null;
    }

    /**
     * Checks if the username is already taken.
     *
     * @param username The username to be checked.
     * @return True if the username is already taken, false otherwise.
     */
    public boolean isUsernameAlreadyTaken(String username) {
        return userController.findUserByUsername(username) != null;
    }

    /**
     * Checks if the password is valid (at least 6 characters long).
     *
     * @param password The password to be checked.
     * @return True if the password is valid, false otherwise.
     */
    public boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    /**
     * Checks if the current password is correct.
     *
     * @param user The user whose password is to be checked.
     * @param currentPassword The current password entered by the user.
     * @return True if the current password is correct, false otherwise.
     */
    public boolean isCurrentPasswordCorrect(User user, String currentPassword) {
        return BCrypt.checkpw(currentPassword, user.getPassword());
    }
}