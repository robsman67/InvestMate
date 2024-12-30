package com.projet.da50.projet_da50.model;

import java.util.*;


public class Lesson {

    private int id;

    private Title title;

    private List<Elements> elements = new ArrayList<>();

    private Tags tag;

    public Lesson() {
    }

    // Getters and setters

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    // Methods to add and remove elements
    public void addElement(Elements element) {
        this.elements.add(element);
    }

    public boolean removeElement(Elements value) {
        // Parcourt la liste et cherche l'élément à supprimer
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i).equals(value)) {
                elements.remove(i);  // Supprime l'élément à l'indice i
                return true;
            }
        }
        return false;  // Si l'élément n'a pas été trouvé
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

