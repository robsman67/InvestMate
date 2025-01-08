package com.projet.da50.projet_da50.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * This class represents a login attempt, tracking the number of attempts and the time of the last attempt.
 */
public class LoginAttempt {
    private int attempts;
    private LocalDateTime lastAttemptTime;

    /**
     * Constructor for LoginAttempt.
     * Initializes the number of attempts to 0 and the last attempt time to null.
     */
    public LoginAttempt() {
        this.attempts = 0;
        this.lastAttemptTime = null;
    }

    /**
     * Gets the number of attempts.
     *
     * @return The number of attempts.
     */
    public int getAttempts() {
        return attempts;
    }

    /**
     * Increments the number of attempts and updates the last attempt time to the current time.
     */
    public void incrementAttempts() {
        this.attempts++;
        this.lastAttemptTime = LocalDateTime.now();
    }

    /**
     * Resets the number of attempts to 0 and the last attempt time to null.
     */
    public void resetAttempts() {
        this.attempts = 0;
        this.lastAttemptTime = null;
    }

    /**
     * Gets the time of the last attempt.
     *
     * @return The time of the last attempt.
     */
    public LocalDateTime getLastAttemptTime() {
        return lastAttemptTime;
    }

    /**
     * Checks if the account is locked based on the number of attempts and the lock duration.
     *
     * @param lockDurationMillis The lock duration in milliseconds.
     * @return True if the account is locked, false otherwise.
     */
    public boolean isLocked(long lockDurationMillis) {
        if (this.lastAttemptTime == null) {
            return false;
        }
        LocalDateTime lockTime = this.lastAttemptTime.plus(lockDurationMillis, ChronoUnit.MILLIS);
        // Check if the number of attempts is greater than or equal to the maximum allowed attempts and if the current time is before the lock time
        int maxAttempts = 5;
        return this.attempts >= maxAttempts && LocalDateTime.now().isBefore(lockTime);
    }
}