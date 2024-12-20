package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.Label;

public class CustomLabel extends Label {

    public CustomLabel(String text) {
        super(text);
        this.getStyleClass().add("form-label");
    }
}