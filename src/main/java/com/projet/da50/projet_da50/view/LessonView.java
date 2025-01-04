package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LessonController;
import com.projet.da50.projet_da50.model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class LessonView extends UI {

    private Stage primaryStage;
    private LessonController lessonController;
    private String title;

    public LessonView(Stage primaryStage, String title) {
        this.primaryStage = primaryStage;
        this.lessonController = new LessonController();
        this.title = title;
    }

    public void show() {
        // Crée un BorderPane pour la structure de la scène
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.getStyleClass().add("main-background");

        // Crée un VBox pour afficher la prévisualisation du contenu de la leçon
        VBox section = new VBox(10); // Espacement entre les éléments
        section.setAlignment(Pos.TOP_LEFT); // Alignement des éléments à gauche
        root.setCenter(new ScrollPane(section)); // Ajoute le VBox dans un ScrollPane

        // Ajoute un titre principal
        Label mainTitle = new Label(title);
        mainTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        section.getChildren().add(mainTitle);

        Lesson lesson = lessonController.getLessonByTitle(title);

        System.out.println("Lesson: " + lesson.toString());

        // Liste des éléments de la leçon
        List<Elements> elementsList = lesson.getElements();

        System.out.println("Elements: " + elementsList);

        // Parcourt les éléments de la leçon et les ajoute à la section
        for (Elements element : elementsList) {
            HBox elementBox = new HBox(10); // Espace entre les éléments dans un HBox
            elementBox.setAlignment(Pos.CENTER_LEFT); // Alignement à gauche

            // Affichage selon le type d'élément
            if (element instanceof Title) {
                Title title = (Title) element;
                Label titleLabel = new Label(title.getContent());
                titleLabel.setStyle(
                        title.getType() == TitleType.MainTitle
                                ? "-fx-font-size: 20px; -fx-font-weight: bold;"
                                : "-fx-font-size: 16px; -fx-font-style: italic;"
                );
                elementBox.getChildren().add(titleLabel);
            } else if (element instanceof Paragraph) {
                Paragraph paragraph = (Paragraph) element;
                Label paragraphLabel = new Label(paragraph.getContent());
                paragraphLabel.setStyle("-fx-font-size: 14px;");
                paragraphLabel.setWrapText(true); // Autoriser le texte à se plier
                elementBox.getChildren().add(paragraphLabel);
            } else if (element instanceof PictureIntegration) {
                PictureIntegration image = (PictureIntegration) element;
                try {
                    ImageView imageView = new ImageView(new javafx.scene.image.Image(image.getContentPath()));
                    imageView.setFitWidth(300); // Définir une largeur fixe
                    imageView.setPreserveRatio(true); // Préserver le ratio de l'image
                    elementBox.getChildren().add(imageView);
                } catch (Exception ex) {
                    Label errorLabel = new Label("Image non disponible : " + image.getContentPath());
                    errorLabel.setStyle("-fx-text-fill: red;");
                    elementBox.getChildren().add(errorLabel);
                }
            } else if (element instanceof VideoIntegration) {
                VideoIntegration video = (VideoIntegration) element;
                String videoPath = video.getContentPath();

                try {
                    Media media = new Media(new File(videoPath).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setFitWidth(300); // Définir une largeur fixe pour la vidéo
                    mediaPlayer.play();
                    elementBox.getChildren().add(mediaView);
                } catch (Exception ex) {
                    Label errorLabel = new Label("Vidéo locale non disponible : " + videoPath);
                    errorLabel.setStyle("-fx-text-fill: red;");
                    elementBox.getChildren().add(errorLabel);
                }
            }

            // Ajoute l'élément dans le HBox à la section
            section.getChildren().add(elementBox);
        }

        // Crée la scène avec le BorderPane comme racine
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Définir la scène sur le Stage et afficher
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Maximiser la fenêtre
        primaryStage.show();
    }
}
