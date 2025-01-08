package com.projet.da50.projet_da50.model;

import javax.persistence.*;

@Entity
@Table(name = "Elements")
@Inheritance(strategy = InheritanceType.JOINED) // Utilisation de la stratégie d'héritage
public abstract class Elements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    @Column(name = "id")
    private Integer id;

    @Column(name = "position")
    private Integer position;

    public Elements() {}

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public abstract String toSave();
}