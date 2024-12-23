package com.projet.da50.projet_da50.model;

public class Title extends Elements{
    private String content;

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
