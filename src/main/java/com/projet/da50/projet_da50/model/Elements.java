package com.projet.da50.projet_da50.model;

public abstract class Elements {
    private int id;

    private String name;

    public Elements() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String toSave();
}
