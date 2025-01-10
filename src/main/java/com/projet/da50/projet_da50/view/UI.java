package com.projet.da50.projet_da50.view;


/**
 * Abstract class to define the UI
 */
public abstract class UI {
    protected static final double WINDOW_WIDTH = 1024;
    protected static final double WINDOW_HEIGHT = 640;

    protected static boolean admin;

    // Getters and setters
    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        UI.admin = admin;
    }

    /**
     * Method to show the UI
     */
    public abstract void show();
}
