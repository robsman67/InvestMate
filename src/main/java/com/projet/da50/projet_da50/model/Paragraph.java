package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "Paragraphs")
public class Paragraph extends Elements{

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "type", nullable = false)
    private ParagraphType type;

    public Paragraph() {}

    public Paragraph(String content, ParagraphType type) {
        this.content = content;
        this.type = type;
    }

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ParagraphType getType() {
        return type;
    }

    public void setType(ParagraphType type) {
        this.type = type;
    }

    @Override
    public String toSave() {
        return "Saving Paragraph: " + content + " [" + type + "]";
    }
}
