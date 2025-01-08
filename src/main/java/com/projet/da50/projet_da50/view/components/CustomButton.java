package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.Button;

/**
 * This class represents a custom button with a default blue style.
 */
public class CustomButton extends Button {

    /**
     * Constructor for CustomButton.
     * Sets the button text and applies the default blue style.
     *
     * @param text The text to be displayed on the button.
     */
    public CustomButton(String text) {
        super(text);
        this.getStyleClass().add("button-blue"); // Default style
    }
}