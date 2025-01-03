package com.projet.da50.projet_da50.controller;


//Not sure to let it like that.
public enum LogAction {
    LOGIN("Login"),
    LOGOUT("Logout"),
    ADD_COURSE("Add Course"),
    DELETE_COURSE("Delete Course"),
    ADD_CREDIT("Add Credit"),
    DELETE_CREDIT("Delete Credit"),
    ADD_USER("Add User"),
    DELETE_USER("Delete User"),
    ADD_LOG("Add Log"),
    DELETE_LOG("Delete Log");

    private final String action;

    LogAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}
