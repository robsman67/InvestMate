package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.PasswordField;

/**
 * This class represents a custom password field with a default form style and preferred width.
 */
public class CustomPasswordField extends PasswordField {

    private static final double PREF_WIDTH = 125; // Preferred width for the password field

    /**
     * Default constructor for CustomPasswordField.
     * Applies the default form style and sets the preferred width.
     */
    public CustomPasswordField() {
        this.getStyleClass().add("form-textfield");
        this.setPrefWidth(PREF_WIDTH);
    }

    /**
     * Constructor for CustomPasswordField with prompt text.
     * Applies the default form style, sets the preferred width, and sets the prompt text.
     *
     * @param promptText The prompt text to be displayed in the password field.
     */
    public CustomPasswordField(String promptText) {
        this();
        this.setPromptText(promptText);
    }
}