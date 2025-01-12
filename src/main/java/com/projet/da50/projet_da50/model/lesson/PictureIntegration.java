package com.projet.da50.projet_da50.model.lesson;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Represents an image element integrated within a lesson.
 * Inherits from {@link Elements} and includes the image data and its content path.
 */
@Entity
@Table(name = "PictureIntegrations")
public class PictureIntegration extends Elements {

    @Lob // Indicates that this field will store large data (e.g., image)
    @Column(name = "imageData", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData; // The binary data of the image

    private String contentPath; // The path to the image content (if applicable)

    /**
     * Default constructor.
     */
    public PictureIntegration() {}

    /**
     * Constructs a PictureIntegration with the given image data.
     *
     * @param imageData The binary data of the image.
     */
    public PictureIntegration(byte[] imageData) {
        this.imageData = imageData;
    }

    /**
     * Constructs a PictureIntegration with the given content path.
     *
     * @param content The path to the image content.
     */
    public PictureIntegration(String content) {
        this.contentPath = content;
    }

    // Getters and setters

    /**
     * Gets the binary data of the image.
     *
     * @return The binary data of the image.
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * Sets the binary data of the image.
     *
     * @param imageData The image data to set.
     */
    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    /**
     * Gets the path to the image content.
     *
     * @return The path to the image content.
     */
    public String getContentPath() {
        return contentPath;
    }

    /**
     * Sets the path to the image content.
     *
     * @param contentPath The content path to set.
     */
    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    /**
     * Returns a string representation for saving the picture.
     *
     * @return A string message indicating the image size.
     */
    @Override
    public String toSave() {
        return "Saving PictureIntegration with image size: " + (imageData != null ? imageData.length : 0) + " bytes";
    }

    /**
     * Returns a string representation of the PictureIntegration object.
     * The image data is partially displayed to avoid printing large binary data.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "PictureIntegration{" +
                "imageData=" + (imageData != null ? Arrays.toString(Arrays.copyOf(imageData, Math.min(imageData.length, 10))) + "..." : "null") +
                '}';
    }
}
