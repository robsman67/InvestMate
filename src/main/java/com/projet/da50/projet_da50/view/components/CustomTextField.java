package com.projet.da50.projet_da50.view.components;

import javafx.scene.control.TextField;

public class CustomTextField extends TextField {

    private static final double PREF_WIDTH = 125; // Largeur préférée pour les champs

    public CustomTextField() {
        this.getStyleClass().add("form-textfield");
        this.setPrefWidth(PREF_WIDTH);
    }

    public CustomTextField(String promptText) {
        this();
        this.setPromptText(promptText);
    }
}