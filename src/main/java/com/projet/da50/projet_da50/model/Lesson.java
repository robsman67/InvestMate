package com.projet.da50.projet_da50.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


public class Lesson {

    private int id;

    private Map<String, Elements> elements = new HashMap<>();

    private Tags tag;

    public Lesson() {
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, Elements> getElements() {
        return elements;
    }

    public void setElements(Map<String, Elements> elements) {
        this.elements = elements;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    // Methods to add and remove elements
    public void addElement(String index, Elements element) {
        this.elements.put(index, element);
    }

    public boolean removeElement(Elements value) {
        Iterator<Map.Entry<String, Elements>> iterator = elements.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Elements> entry = iterator.next();
            if (entry.getValue().equals(value)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", elements=" + elements +
                ", tag=" + tag +
                '}';
    }
}

