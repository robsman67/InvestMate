package com.projet.da50.projet_da50.model;

import javax.persistence.*;

/**
 * Represents a paragraph element within a lesson.
 * Inherits from {@link Elements} and includes content and a type.
 */
@Entity
@Table(name = "Paragraphs")
public class Paragraph extends Elements {

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // The content of the paragraph

    @Column(name = "type", nullable = false)
    private ParagraphType type; // The type of the paragraph (e.g., introduction, conclusion)

    /**
     * Default constructor.
     */
    public Paragraph() {}

    /**
     * Constructs a paragraph with specified content and type.
     *
     * @param content The content of the paragraph.
     * @param type The type of the paragraph.
     */
    public Paragraph(String content, ParagraphType type) {
        this.content = content;
        this.type = type;
    }

    // Getters and setters

    /**
     * Gets the content of the paragraph.
     *
     * @return The content of the paragraph.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the paragraph.
     *
     * @param content The content to set for the paragraph.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the type of the paragraph.
     *
     * @return The type of the paragraph.
     */
    public ParagraphType getType() {
        return type;
    }

    /**
     * Sets the type of the paragraph.
     *
     * @param type The type to set for the paragraph.
     */
    public void setType(ParagraphType type) {
        this.type = type;
    }

    /**
     * Returns a string representation for saving the paragraph.
     *
     * @return A string message indicating the paragraph content and its type.
     */
    @Override
    public String toSave() {
        return "Saving Paragraph: " + content + " [" + type + "]";
    }
}
