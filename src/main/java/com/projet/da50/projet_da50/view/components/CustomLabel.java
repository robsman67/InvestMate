package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.Label;

/**
 * This class represents a custom label with a default form style.
 */
public class CustomLabel extends Label {

    /**
     * Constructor for CustomLabel.
     * Sets the label text and applies the default form style.
     *
     * @param text The text to be displayed on the label.
     */
    public CustomLabel(String text) {
        super(text);
        this.getStyleClass().add("form-label");
    }
}