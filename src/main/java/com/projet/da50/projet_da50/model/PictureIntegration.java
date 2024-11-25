package com.projet.da50.projet_da50.model;

public class PictureIntegration extends Elements {
    private String contentPath;

    public PictureIntegration() {}

    public PictureIntegration(String contentPath) {
        this.contentPath = contentPath;
    }

    // Getters and setters
    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    @Override
    public String toSave() {
        return "Saving PictureIntegration: " + contentPath;
    }
}
