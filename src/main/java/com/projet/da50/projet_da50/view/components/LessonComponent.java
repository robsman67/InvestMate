package com.projet.da50.projet_da50.view.components;

import com.projet.da50.projet_da50.model.Lesson;
import com.projet.da50.projet_da50.view.LessonView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LessonComponent extends HBox {

    public LessonComponent(Stage primaryStage, Lesson lesson) {
        // Forcer l'extension sur toute la largeur
        setPrefWidth(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);

        // Configuration de base du HBox
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setPadding(new Insets(10));
        setStyle(
                "-fx-background-color: #f5f5f5; " + // Couleur de fond clair
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #d1d1d1; " + // Couleur du cadre
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px;"
        );

        // Création des labels pour le contenu
        Label nameLabel = new Label("Title: " + lesson.getTitle());
        nameLabel.setFont(Font.font(16));
        nameLabel.setTextFill(Color.BLACK);

        Label tagLabel = new Label("Tag: " + lesson.getTag());
        tagLabel.setFont(Font.font(14));
        tagLabel.setTextFill(Color.GRAY);

        // Espacement flexible pour équilibrer les contenus
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Création du bouton "Lire" à droite
        Button readButton = new Button("read");
        readButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        readButton.setOnAction(event -> {
            System.out.println("Clicked on " + lesson.getTitle());
            new LessonView(primaryStage, lesson.getTitle()).show();
        });

        // Ajout des éléments au HBox
        getChildren().addAll(nameLabel, tagLabel, spacer,readButton);
    }
}