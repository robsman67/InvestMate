package com.projet.da50.projet_da50.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "PictureIntegrations")
public class PictureIntegration extends Elements {

    @Lob // Indique que ce champ stockera des données volumineuses
    @Column(name = "imageData", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData;

    private String contentPath;;

    public PictureIntegration() {}

    public PictureIntegration(byte[] imageData) {
        this.imageData = imageData;
    }

    public PictureIntegration(String content) {
        this.contentPath = content;
    }

    // Getters et setters
    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    @Override
    public String toSave() {
        return "Saving PictureIntegration with image size: " + (imageData != null ? imageData.length : 0) + " bytes";
    }

    @Override
    public String toString() {
        return "PictureIntegration{" +
                "imageData=" + (imageData != null ? Arrays.toString(Arrays.copyOf(imageData, Math.min(imageData.length, 10))) + "..." : "null") +
                '}';
    }
}
