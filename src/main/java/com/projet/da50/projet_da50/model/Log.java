package com.projet.da50.projet_da50.model;

import com.projet.da50.projet_da50.controller.LogAction;
import com.sun.jna.platform.win32.Sspi;

public class Log {
    private int id;
    private int userId;
    private LogAction action;
    private String details;
    private Sspi.TimeStamp timestamp;

    public Log(int id, int userId, LogAction action, String details) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.details = details;
        timestamp = new Sspi.TimeStamp();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public LogAction getAction() {
        return action;
    }

    public String getDetails() {
        return details;
    }

    public Sspi.TimeStamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Sspi.TimeStamp timestamp) {
        this.timestamp = timestamp;
    }
}
