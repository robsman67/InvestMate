package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.TextField;

/**
 * This class represents a custom text field with a default form style and preferred width.
 */
public class CustomTextField extends TextField {

    private static final double PREF_WIDTH = 125; // Preferred width for the text field

    /**
     * Default constructor for CustomTextField.
     * Applies the default form style and sets the preferred width.
     */
    public CustomTextField() {
        this.getStyleClass().add("form-textfield");
        this.setPrefWidth(PREF_WIDTH);
    }

    /**
     * Constructor for CustomTextField with prompt text.
     * Applies the default form style, sets the preferred width, and sets the prompt text.
     *
     * @param promptText The prompt text to be displayed in the text field.
     */
    public CustomTextField(String promptText) {
        this();
        this.setPromptText(promptText);
    }
}