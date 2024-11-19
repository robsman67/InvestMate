package com.projet.da50.projet_da50.model;

public class Title extends Elements{
    private String content;

    public Title() {}

    // Getters and setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toSave() {
        return "Saving Title: " + content;
    }
}
