package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.Button;

public class CustomButton extends Button {

    public CustomButton(String text) {
        super(text);
        this.getStyleClass().add("button-blue"); // Style par défaut
    }
}