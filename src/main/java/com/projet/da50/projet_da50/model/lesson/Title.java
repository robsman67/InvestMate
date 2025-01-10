package com.projet.da50.projet_da50.model.lesson;

import javax.persistence.*;

/**
 * Represents a title element within a lesson.
 * Inherits from {@link Elements} and includes content and type.
 */
@Entity
@Table(name = "Titles")
public class Title extends Elements {

    @Column(name = "content", nullable = false)
    private String content; // The content of the title

    @Column(name = "type", nullable = false)
    private TitleType type; // The type of the title (e.g., main title, subtitle)

    /**
     * Default constructor.
     */
    public Title() {}

    /**
     * Constructs a Title with the specified content and type.
     *
     * @param content The content of the title.
     * @param type The type of the title (e.g., main title, subtitle).
     */
    public Title(String content, TitleType type) {
        this.content = content;
        this.type = type;
    }

    // Getters and setters

    /**
     * Gets the content of the title.
     *
     * @return The content of the title.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the title.
     *
     * @param content The content to set for the title.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the type of the title.
     *
     * @return The type of the title.
     */
    public TitleType getType() {
        return type;
    }

    /**
     * Sets the type of the title.
     *
     * @param type The type to set for the title.
     */
    public void setType(TitleType type) {
        this.type = type;
    }

    /**
     * Returns a string representation for saving the title.
     *
     * @return A string message indicating the title content and its type.
     */
    @Override
    public String toSave() {
        return "Saving Title: " + content + " [" + type + "]";
    }
}
