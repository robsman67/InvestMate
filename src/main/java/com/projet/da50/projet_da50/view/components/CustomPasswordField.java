package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.PasswordField;

public class CustomPasswordField extends PasswordField {

    private static final double PREF_WIDTH = 125; // Largeur préférée pour les champs

    public CustomPasswordField() {
        this.getStyleClass().add("form-textfield");
        this.setPrefWidth(PREF_WIDTH);
    }

    public CustomPasswordField(String promptText) {
        this();
        this.setPromptText(promptText);
    }
}