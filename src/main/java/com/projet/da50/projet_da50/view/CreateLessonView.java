package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LessonController;
import com.projet.da50.projet_da50.model.*;
import com.projet.da50.projet_da50.view.components.CustomButton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.media.*;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.util.List;


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
        root.getStyleClass().add("main-background");

        // Haut : Formulaire pour ajouter des éléments
        VBox addElementSection = new VBox(10);
        addElementSection.setPadding(new Insets(10));
        addElementSection.setAlignment(Pos.CENTER_LEFT);

        // Champ pour le nom de la leçon
        Label nameLabel = new Label("Nom de la leçon :");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField nameField = new TextField();
        addElementSection.getChildren().addAll(nameLabel, nameField);

        // Sélection du type d'élément
        Label tagTypeLabel = new Label("Tag:");
        tagTypeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        ComboBox<Tags> tagTypeComboBox = new ComboBox<>();
        tagTypeComboBox.getItems().addAll(Tags.BEGINNER, Tags.INTERMEDIATE, Tags.EXPERT);
        tagTypeComboBox.setValue(Tags.BEGINNER);

        // Sélection du type d'élément
        Label elementTypeLabel = new Label("Type d'élément :");
        elementTypeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        ComboBox<String> elementTypeComboBox = new ComboBox<>();
        elementTypeComboBox.getItems().addAll("Sous-Titre", "Paragraphe", "Image", "Vidéo");
        elementTypeComboBox.setValue("Paragraphe");


        // Champ pour le contenu de l'élément
        Label contentLabel = new Label("Contenu de l'élément :");
        contentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");
        TextField contentField = new TextField();

        ComboBox<TitleType> titleTypeComboBox = new ComboBox<>();
        titleTypeComboBox.getItems().addAll(TitleType.SubTitle1, TitleType.SubTitle2);
        titleTypeComboBox.setValue(TitleType.SubTitle1);

        // Options pour le type de paragraphe ou de sous-titre
        ComboBox<ParagraphType> paragraphTypeComboBox = new ComboBox<>();
        paragraphTypeComboBox.getItems().addAll(ParagraphType.INTRO, ParagraphType.TEXT, ParagraphType.CCL);
        paragraphTypeComboBox.setValue(ParagraphType.INTRO);

        addElementSection.getChildren().addAll(tagTypeLabel,tagTypeComboBox,elementTypeLabel,elementTypeComboBox, contentLabel, contentField);

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
            } else if (selectedType.equals("Image") || selectedType.equals("Vidéo")) {
                Button chooseImageButton = new CustomButton("Choose Image");
                chooseImageButton.getStyleClass().add("button-blue");
                chooseImageButton.setOnAction(event -> {
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Sélectionner a file");
                    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg", "*.gif","*.mp4","*.avi","*.flv","*.mov"));

                    File selectedFile = fileChooser.showOpenDialog(stage); // Afficher le dialogue
                    if (selectedFile != null) {
                        contentField.setText(selectedFile.getAbsolutePath()); // Remplir le champ de contenu avec le chemin du fichier
                    }
                });
                typeOptionBox.getChildren().add(chooseImageButton);
            }
        });

        // Bouton pour ajouter un élément
        Button addButton = new CustomButton("Add");
        addButton.getStyleClass().add("button-blue");
        addButton.setOnAction(e -> {
            String content = contentField.getText();
            String elementType = elementTypeComboBox.getValue();

            if (content.isEmpty()) {
                showAlert("Erreur", "Le contenu de l'élément ne peut pas être vide.");
                return;
            }

            // Ajouter l'élément via le contrôleur
            switch (elementType) {
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
                    try {
                        lessonController.createImage(content);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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

        // Prévisualisation avec défilement
        VBox previewContent = new VBox(10); // Le contenu défilable
        previewContent.setPadding(new Insets(10));
        previewContent.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");

        ScrollPane previewScrollPane = new ScrollPane(previewContent);
        previewScrollPane.setFitToWidth(true); // Adapter à la largeur
        previewScrollPane.setStyle("-fx-background: #ffffff;");

        // Section centrale
        VBox previewSection = new VBox(10);
        previewSection.getChildren().add(previewScrollPane);
        previewSection.setPadding(new Insets(10));

        // Bouton pour sauvegarder la leçon
        Button saveButton = new CustomButton("Save");
        saveButton.getStyleClass().add("button-blue");
        saveButton.setOnAction(e -> {
            String lessonName = nameField.getText();
            if (lessonController.validateInputs(lessonName)) {
                lessonController.createMainTitle(lessonName);
                lessonController.setTag(tagTypeComboBox.getValue());
                lessonController.createLesson();
                new MainMenuView(stage).show();
            }
        });

        // Bouton pour sauvegarder la leçon
        Button BackButton = new CustomButton("back to the main menu");
        BackButton.getStyleClass().add("button-blue");
        BackButton.setOnAction(e -> {
            new MainMenuView(stage).show();
        });

        VBox BtnContent = new VBox(10); // Le contenu défilable
        BtnContent.getChildren().add(saveButton);
        BtnContent.getChildren().add(BackButton);
        BtnContent.setPadding(new Insets(10));

        root.setTop(addElementSection);
        root.setCenter(previewSection);
        root.setBottom(BtnContent);
        BorderPane.setAlignment(saveButton, Pos.CENTER);
        BorderPane.setMargin(saveButton, new Insets(10));

        // Créer et afficher la scène
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setTitle("Lesson Creator");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();

        // Initialiser la méthode updatePreview
        updatePreview();
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
        // Récupère la section de prévisualisation
        VBox previewSection = (VBox) ((ScrollPane) ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().get(0)).getContent();
        previewSection.getChildren().clear();

        // Ajoute un titre à la prévisualisation
        Label previewTitle = new Label("Prévisualisation de la leçon :");
        previewTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        previewSection.getChildren().add(previewTitle);

        List<Elements> lesson = lessonController.getLesson().getElements();

        // Parcourt les éléments de la leçon
        for (int i = 0; i < lessonController.getLesson().getElements().size(); i++) {

            Elements element = lesson.get(i);

            // Conteneur pour chaque élément et ses boutons
            HBox elementBox = new HBox();
            elementBox.setSpacing(10);
            elementBox.setAlignment(Pos.CENTER_LEFT);

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
                paragraphLabel.setWrapText(true);
                elementBox.getChildren().add(paragraphLabel);
            } else if (element instanceof PictureIntegration) {
                PictureIntegration image = (PictureIntegration) element;
                try {
                    ImageView imageView = new ImageView(new javafx.scene.image.Image(image.getContentPath()));
                    imageView.setFitWidth(300);
                    imageView.setPreserveRatio(true);
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
                    mediaView.setFitWidth(300);
                    mediaPlayer.play();
                    elementBox.getChildren().add(mediaView);
                } catch (Exception ex) {
                    Label errorLabel = new Label("Vidéo locale non disponible : " + videoPath);
                    errorLabel.setStyle("-fx-text-fill: red;");
                    elementBox.getChildren().add(errorLabel);
                }
            }


            // Ajoute des boutons flèches pour échanger les éléments
            Button upButton = new Button("↑");
            upButton.setStyle("-fx-font-size: 14px;");
            int finalI = i;
            upButton.setOnAction(event -> {
                if (finalI > 0) {  // Vérifie si l'élément n'est pas déjà au début
                    lessonController.swapElements(lesson, finalI, finalI-1);  // Appel à une méthode pour échanger les éléments
                    updatePreview();  // Met à jour la prévisualisation
                }
            });

            Button downButton = new Button("↓");
            downButton.setStyle("-fx-font-size: 14px;");
            downButton.setOnAction(event -> {
                if (finalI < lessonController.getLesson().getElements().size() - 1) {  // Vérifie si l'élément n'est pas déjà à la fin
                    lessonController.swapElements(lesson, finalI, finalI+1);  // Appel à une méthode pour échanger les éléments
                    updatePreview();  // Met à jour la prévisualisation
                }
            });

            // Ajoute les boutons flèches à la boîte
            elementBox.getChildren().addAll(upButton, downButton);

            // Ajoute un bouton "Supprimer"
            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-size: 12px;");
            deleteButton.setOnAction(event -> {
                // Supprime l'élément de la leçon
                lessonController.removeElement(element);
                // Met à jour la prévisualisation
                updatePreview();
            });

            // Ajoute le bouton "Supprimer" à la boîte
            elementBox.getChildren().add(deleteButton);

            // Ajoute l'élément à la section de prévisualisation
            previewSection.getChildren().add(elementBox);
        }
    }
}