package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "Titles")
public class Title extends Elements{

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type", nullable = false)
    private TitleType type;

    public Title() {}

    public Title(String content, TitleType type) {
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

    public TitleType getType() {
        return type;
    }

    public void setType(TitleType type) {
        this.type = type;
    }

    @Override
    public String toSave() {
        return "Saving Paragraph: " + content + " [" + type + "]";
    }
}
