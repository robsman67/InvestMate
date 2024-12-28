package com.projet.da50.projet_da50.view;

public abstract class UI {
    protected static final double WINDOW_WIDTH = 1024;
    protected static final double WINDOW_HEIGHT = 640;

    protected static boolean admin;

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        UI.admin = admin;
    }

    // Abstract method to display the view content
    public abstract void show();
}
