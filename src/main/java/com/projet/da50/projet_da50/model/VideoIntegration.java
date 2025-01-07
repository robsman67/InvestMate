package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "VideoIntegrations")
public class VideoIntegration extends Elements {

    @Column(name = "contentPath", nullable = false)
    private String contentPath;

    @Lob // Indique que ce champ stockera des données volumineuses
    @Column(name = "videoData", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] videoData;

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

    public byte[] getVideoData() {
        return videoData;
    }

    public void setVideoData(byte[] videoData) {
        this.videoData = videoData;
    }

    @Override
    public String toSave() {
        return "Saving VideoIntegration: " + contentPath;
    }
}
