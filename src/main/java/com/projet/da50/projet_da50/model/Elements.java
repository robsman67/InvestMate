package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "Elements")
@Inheritance(strategy = InheritanceType.JOINED) // Utilisation de la stratégie d'héritage
public abstract class Elements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    @Column(name = "id")
    private int id;

    @Column(name = "name", unique = true, nullable = false)
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
