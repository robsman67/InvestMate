package com.projet.da50.projet_da50.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LoginAttempt {
    private int attempts;
    private LocalDateTime lastAttemptTime;

    public LoginAttempt() {
        this.attempts = 0;
        this.lastAttemptTime = null;
    }

    public int getAttempts() {
        return attempts;
    }

    public void incrementAttempts() {
        this.attempts++;
        this.lastAttemptTime = LocalDateTime.now();
    }

    public void resetAttempts() {
        this.attempts = 0;
        this.lastAttemptTime = null;
    }

    public LocalDateTime getLastAttemptTime() {
        return lastAttemptTime;
    }

    public boolean isLocked(long lockDurationMillis) {
        if (this.lastAttemptTime == null) {
            return false;
        }
        LocalDateTime lockTime = this.lastAttemptTime.plus(lockDurationMillis, ChronoUnit.MILLIS);
        // Check if the number of attempts is greater than 5 and if the current time is before the lock time
        return this.attempts >= 5 && LocalDateTime.now().isBefore(lockTime);
    }
}