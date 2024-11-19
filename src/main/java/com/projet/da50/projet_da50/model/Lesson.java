package com.projet.da50.projet_da50.model;

import java.util.HashMap;
import java.util.Map;

public class Lesson {

    private int id;

    private Map<Integer, Elements> elements = new HashMap<>();

    public Lesson() {}

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Integer, Elements> getElements() {
        return elements;
    }

    public void setElements(Map<Integer, Elements> elements) {
        this.elements = elements;
    }

    // Methods to add and remove elements
    public void addElement(Elements element) {
        this.elements.put(element.getId(), element);
    }

    public void removeElement(int elementId) {
        this.elements.remove(elementId);
    }
}
