package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LessonController;
import com.projet.da50.projet_da50.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.media.*;

public class CreateLessonView extends UI {
    private Stage stage;
    private LessonController lessonController; // Intégration du contrôleur
    private ObservableList<Elements> elementsList; // Liste observable pour afficher les éléments dynamiquement

    public CreateLessonView(Stage stage) {
        this.stage = stage;
        this.lessonController = new LessonController(); // Instanciation du contrôleur
        this.elementsList = FXCollections.observableArrayList(); // Liste des éléments
    }

    public void show() {
        // Grille principale
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Haut : Formulaire pour ajouter des éléments
        VBox addElementSection = new VBox(10);
        addElementSection.setPadding(new Insets(10));
        addElementSection.setAlignment(Pos.CENTER_LEFT);

        // Champ pour le nom de la leçon
        Label nameLabel = new Label("Nom de la leçon :");
        TextField nameField = new TextField();
        addElementSection.getChildren().addAll(nameLabel, nameField);

        // Sélection du type d'élément
        Label elementTypeLabel = new Label("Type d'élément :");
        ComboBox<String> elementTypeComboBox = new ComboBox<>();
        elementTypeComboBox.getItems().addAll("Sous-Titre", "Paragraphe", "Image", "Vidéo");
        elementTypeComboBox.setValue("Titre Principal");

        // Champ pour le contenu de l'élément
        Label contentLabel = new Label("Contenu de l'élément :");
        TextField contentField = new TextField();

        // Options pour le type de paragraphe ou de sous-titre
        ComboBox<ParagraphType> paragraphTypeComboBox = new ComboBox<>();
        paragraphTypeComboBox.getItems().addAll(ParagraphType.INTRO, ParagraphType.TEXT, ParagraphType.CCL);
        paragraphTypeComboBox.setValue(ParagraphType.TEXT);

        ComboBox<TitleType> titleTypeComboBox = new ComboBox<>();
        titleTypeComboBox.getItems().addAll(TitleType.SubTitle1, TitleType.SubTitle2);
        titleTypeComboBox.setValue(TitleType.SubTitle1);

        addElementSection.getChildren().addAll(elementTypeLabel, elementTypeComboBox, contentLabel, contentField);

        // Ajouter un champ dynamique pour le type
        HBox typeOptionBox = new HBox(10);
        typeOptionBox.getChildren().add(paragraphTypeComboBox); // Par défaut, afficher l'option pour le type de paragraphe
        addElementSection.getChildren().add(typeOptionBox);

        // Dynamiquement changer le type d'option
        elementTypeComboBox.setOnAction(e -> {
            String selectedType = elementTypeComboBox.getValue();
            typeOptionBox.getChildren().clear();
            if (selectedType.equals("Sous-Titre")) {
                typeOptionBox.getChildren().add(titleTypeComboBox);
            } else if (selectedType.equals("Paragraphe")) {
                typeOptionBox.getChildren().add(paragraphTypeComboBox);
            }
        });

        // Bouton pour ajouter un élément
        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            String content = contentField.getText();
            String elementType = elementTypeComboBox.getValue();

            if (content.isEmpty()) {
                showAlert("Erreur", "Le contenu de l'élément ne peut pas être vide.");
                return;
            }

            // Ajouter l'élément via le contrôleur
            switch (elementType) {
                case "Titre Principal":
                    lessonController.createMainTitle(content);
                    elementsList.add(new Title(content, TitleType.MainTitle));
                    break;
                case "Sous-Titre":
                    TitleType titleType = titleTypeComboBox.getValue();
                    lessonController.createSubTitle(content, titleType);
                    elementsList.add(new Title(content, titleType));
                    break;
                case "Paragraphe":
                    ParagraphType paragraphType = paragraphTypeComboBox.getValue();
                    lessonController.createParagraph(content, paragraphType);
                    elementsList.add(new Paragraph(content, paragraphType));
                    break;
                case "Image":
                    lessonController.createImage(content);
                    elementsList.add(new PictureIntegration(content));
                    break;
                case "Vidéo":
                    lessonController.createVideo(content);
                    elementsList.add(new VideoIntegration(content));
                    break;
                default:
                    break;
            }

            contentField.clear(); // Réinitialiser le champ de contenu
            updatePreview(); // Mettre à jour la prévisualisation
        });

        addElementSection.getChildren().add(addButton);

        // Centre : Prévisualisation de la leçon
        VBox previewSection = new VBox(10);
        previewSection.setPadding(new Insets(10));
        previewSection.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");
        Label previewTitle = new Label("Prévisualisation de la leçon :");
        previewTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        previewSection.getChildren().add(previewTitle);

        // Bouton pour supprimer un élément sélectionné
        Button deleteButton = new Button("Supprimer un élément");
        deleteButton.setOnAction(e -> {
            // Supprime le dernier élément ajouté (pour simplifier, ou vous pouvez ajouter une sélection)
            if (!elementsList.isEmpty()) {
                Elements lastElement = elementsList.get(elementsList.size() - 1);
                lessonController.removeElement(lastElement);
                elementsList.remove(lastElement);
                updatePreview(); // Mettre à jour la prévisualisation
            }
        });

        // Ajout du bouton de suppression à la section
        previewSection.getChildren().add(deleteButton);

        // Section des éléments
        VBox elementsSection = new VBox(10, previewSection);
        elementsSection.setPadding(new Insets(10));

        // Bouton pour sauvegarder la leçon
        Button saveButton = new Button("Sauvegarder la leçon");
        saveButton.setOnAction(e -> {
            String lessonName = nameField.getText();
            if (lessonController.validateInputs(lessonName)) {
                lessonController.saveLesson(); // Sauvegarder la leçon
                showAlert("Succès", "La leçon a été sauvegardée avec succès !");
            }
        });

        root.setTop(addElementSection);
        root.setCenter(elementsSection);
        root.setBottom(saveButton);
        BorderPane.setAlignment(saveButton, Pos.CENTER);
        BorderPane.setMargin(saveButton, new Insets(10));

        // Créer et afficher la scène
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Créer une Leçon");
        stage.setScene(scene);
        stage.show();
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updatePreview() {
        VBox previewSection = (VBox) ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().get(0);
        previewSection.getChildren().clear();
        Label previewTitle = new Label("Prévisualisation de la leçon :");
        previewTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        previewSection.getChildren().add(previewTitle);

        for (Elements element : lessonController.getLesson().getElements().values()) {
            if (element instanceof Title) {
                Title title = (Title) element;
                Label titleLabel = new Label(title.getContent());
                titleLabel.setStyle(
                        title.getType() == TitleType.MainTitle
                                ? "-fx-font-size: 20px; -fx-font-weight: bold;"
                                : "-fx-font-size: 16px; -fx-font-style: italic;"
                );
                previewSection.getChildren().add(titleLabel);
            } else if (element instanceof Paragraph) {
                Paragraph paragraph = (Paragraph) element;
                Label paragraphLabel = new Label(paragraph.getContent());
                paragraphLabel.setStyle("-fx-font-size: 14px;");
                paragraphLabel.setWrapText(true);
                previewSection.getChildren().add(paragraphLabel);
            } else if (element instanceof PictureIntegration) {
                PictureIntegration image = (PictureIntegration) element;
                try {
                    ImageView imageView = new ImageView(new javafx.scene.image.Image(image.getContentPath()));
                    imageView.setFitWidth(300);
                    imageView.setPreserveRatio(true);
                    previewSection.getChildren().add(imageView);
                } catch (Exception ex) {
                    Label errorLabel = new Label("Image non disponible : " + image.getContentPath());
                    errorLabel.setStyle("-fx-text-fill: red;");
                    previewSection.getChildren().add(errorLabel);
                }
            } else if (element instanceof VideoIntegration) {
                VideoIntegration video = (VideoIntegration) element;
                try {
                    // Lecture de la vidéo avec MediaPlayer
                    Media media = new Media(video.getContentPath());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView = new MediaView(mediaPlayer);
                    mediaView.setFitWidth(300); // Ajuster la taille
                    mediaPlayer.play();
                    previewSection.getChildren().add(mediaView);
                } catch (Exception ex) {
                    Label errorLabel = new Label("Vidéo non disponible : " + video.getContentPath());
                    errorLabel.setStyle("-fx-text-fill: red;");
                    previewSection.getChildren().add(errorLabel);
                }
            }
        }
    }
}