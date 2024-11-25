package com.projet.da50.projet_da50.model;

public class VideoIntegration extends Elements {
    private String contentPath;

    public VideoIntegration() {}

    public VideoIntegration(String contentPath) {
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
        return "Saving VideoIntegration: " + contentPath;
    }
}
