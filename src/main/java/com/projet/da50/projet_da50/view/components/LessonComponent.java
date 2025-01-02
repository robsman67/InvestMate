package com.projet.da50.projet_da50.view.components;

import com.projet.da50.projet_da50.model.Lesson;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LessonComponent extends HBox {

    public LessonComponent(Lesson lesson) {
        // Initialisation du HBox pour disposer les éléments horizontalement
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);

        // Création des labels pour le nom de la leçon et son tag
        Label nameLabel = new Label("Title: " + lesson.getTitle());
        nameLabel.setFont(Font.font(14));
        nameLabel.setTextFill(Color.BLACK);

        Label tagLabel = new Label("Tag: " + lesson.getTag());
        tagLabel.setFont(Font.font(12));
        tagLabel.setTextFill(Color.GRAY);

        // Ajout des labels au HBox
        getChildren().addAll(nameLabel, tagLabel);
    }
}