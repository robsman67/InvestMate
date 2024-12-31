package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "VideoIntegrations")
public class VideoIntegration extends Elements {

    @Column(name = "contentPath", nullable = false)
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
