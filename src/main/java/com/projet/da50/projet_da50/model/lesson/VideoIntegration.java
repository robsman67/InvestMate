package com.projet.da50.projet_da50.model.lesson;

import javax.persistence.*;

/**
 * Represents a video element within a lesson.
 * Inherits from {@link Elements} and includes the content path and video data.
 */
@Entity
@Table(name = "VideoIntegrations")
public class VideoIntegration extends Elements {

    @Column(name = "contentPath", nullable = false)
    private String contentPath; // The path or URL to the video content

    @Lob // Indicates that this field stores large binary data
    @Column(name = "videoData", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] videoData; // The binary data of the video

    /**
     * Default constructor.
     */
    public VideoIntegration() {}

    /**
     * Constructs a VideoIntegration with the specified content path.
     *
     * @param contentPath The path to the video content (e.g., file path or URL).
     */
    public VideoIntegration(String contentPath) {
        this.contentPath = contentPath;
    }

    // Getters and setters

    /**
     * Gets the path or URL to the video content.
     *
     * @return The content path for the video.
     */
    public String getContentPath() {
        return contentPath;
    }

    /**
     * Sets the path or URL to the video content.
     *
     * @param contentPath The content path to set for the video.
     */
    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    /**
     * Gets the binary data for the video.
     *
     * @return The video data as a byte array.
     */
    public byte[] getVideoData() {
        return videoData;
    }

    /**
     * Sets the binary data for the video.
     *
     * @param videoData The video data to set.
     */
    public void setVideoData(byte[] videoData) {
        this.videoData = videoData;
    }

    /**
     * Returns a string representation for saving the video integration.
     *
     * @return A string message indicating the content path of the video.
     */
    @Override
    public String toSave() {
        return "Saving VideoIntegration: " + contentPath;
    }
}
