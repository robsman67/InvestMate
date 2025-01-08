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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A view for creating and managing lessons.
 * This class provides an interface to create lessons by adding titles, paragraphs, images, and videos,
 * and allows for previewing and reordering elements within the lesson.
 */
public class CreateLessonView extends UI {

    private final Stage stage; // The stage on which the view is displayed
    private final LessonController lessonController; // Controller for handling lesson logic
    private final ObservableList<Elements> elementsList; // List of elements for dynamic updates
    private final Boolean isUpdate;

    private TextField nameField;
    private ComboBox<Tags> tagTypeComboBox;


    /**
     * Constructor for CreateLessonView.
     *
     * @param stage The main stage of the application.
     */
    public CreateLessonView(Stage stage) {
        this.stage = stage;
        this.lessonController = new LessonController();
        this.elementsList = FXCollections.observableArrayList();
        this.isUpdate = false;
    }

    public CreateLessonView(Stage stage, Lesson lesson) {
        this.stage = stage;
        this.lessonController = new LessonController(lesson);
        this.elementsList = FXCollections.observableArrayList();
        this.isUpdate = true;
    }

    /**
     * Displays the main lesson creation interface.
     */
    public void show() {
        // Root layout setup
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.getStyleClass().add("main-background");

        // Top section: Add elements form
        VBox addElementSection = createAddElementSection();
        root.setTop(addElementSection);

        // Center section: Preview area
        VBox previewSection = createPreviewSection();
        root.setCenter(previewSection);

        // Bottom section: Action buttons
        VBox actionButtons = createActionButtons();
        root.setBottom(actionButtons);

        // Set up the scene and stage
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        stage.setTitle("Lesson Creator");
        stage.setScene(scene);
        stage.show();

        // Initialize preview
        updatePreview();
    }

    /**
     * Creates the section for adding elements to the lesson.
     *
     * @return A VBox containing the add element form.
     */
    private VBox createAddElementSection() {
        VBox addElementSection = new VBox(10);
        addElementSection.setAlignment(Pos.CENTER_LEFT);

        // Lesson name input
        Label nameLabel = createStyledLabel("Lesson Name:", 16, true, "white");
        this.nameField = new TextField();
        addElementSection.getChildren().addAll(nameLabel, nameField);

        // Element tag selection
        Label tagTypeLabel = createStyledLabel("Tag:", 16, true, "white");
        this.tagTypeComboBox = new ComboBox<>();
        tagTypeComboBox.getItems().addAll(Tags.BEGINNER, Tags.INTERMEDIATE, Tags.EXPERT);
        tagTypeComboBox.setValue(Tags.BEGINNER);

        // Element type selection
        Label elementTypeLabel = createStyledLabel("Element Type:", 16, true, "white");
        ComboBox<String> elementTypeComboBox = new ComboBox<>();
        elementTypeComboBox.getItems().addAll("Sub-Title", "Paragraph", "Image", "Video");
        elementTypeComboBox.setValue("---");

        // Content input
        Label contentLabel = createStyledLabel("Content:", 16, true, "white");
        TextField contentField = new TextField();

        // Dynamic options for element type
        HBox typeOptionBox = new HBox(10);
        ComboBox<TitleType> titleTypeComboBox = new ComboBox<>();
        titleTypeComboBox.getItems().addAll(TitleType.SubTitle1, TitleType.SubTitle2);
        titleTypeComboBox.setValue(TitleType.SubTitle1);
        ComboBox<ParagraphType> paragraphTypeComboBox = new ComboBox<>();
        paragraphTypeComboBox.getItems().addAll(ParagraphType.INTRO, ParagraphType.TEXT, ParagraphType.CCL);
        paragraphTypeComboBox.setValue(ParagraphType.INTRO);

        elementTypeComboBox.setOnAction(e -> updateDynamicOptions(typeOptionBox, elementTypeComboBox, titleTypeComboBox, paragraphTypeComboBox, contentField));

        addElementSection.getChildren().addAll(tagTypeLabel, tagTypeComboBox, elementTypeLabel, elementTypeComboBox, contentLabel, contentField, typeOptionBox);

        // Add element button
        Button addButton = new CustomButton("Add");
        addButton.setOnAction(e -> {
            try {
                addElement(contentField, elementTypeComboBox, titleTypeComboBox, paragraphTypeComboBox);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addElementSection.getChildren().add(addButton);

        // Add a separator
        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));
        addElementSection.getChildren().add(separator);

        return addElementSection;
    }

    /**
     * Creates the preview section for displaying the lesson.
     *
     * @return A VBox containing the preview section.
     */
    private VBox createPreviewSection() {
        // Content area for preview elements
        VBox previewContent = new VBox(10);
        previewContent.setPadding(new Insets(10));
        previewContent.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10; -fx-border-color: #dcdcdc; -fx-border-width: 1;");

        // Add a scroll pane to the content area
        ScrollPane previewScrollPane = new ScrollPane(previewContent);
        previewScrollPane.setFitToWidth(true);
        previewScrollPane.setStyle("-fx-background: #ffffff;");

        // Container for the preview section
        VBox previewSection = new VBox(10);
        previewSection.setMinHeight(300);
        previewSection.getChildren().add(previewScrollPane);
        previewSection.setPadding(new Insets(10));

        return previewSection;
    }

    /**
     * Creates the action buttons at the bottom of the interface.
     *
     * @return A VBox containing action buttons.
     */
    private VBox createActionButtons() {
        // Container for action buttons
        VBox buttonSection = new VBox(10);
        buttonSection.setPadding(new Insets(10));
        buttonSection.setAlignment(Pos.CENTER);

        // Save and back buttons
        Button saveButton = new CustomButton("Save");
        saveButton.setOnAction(e ->
        {
            if (isUpdate) {
                updateLesson();
            } else {
                saveLesson();
            }
        });

        // Back to main menu button
        Button backButton = new CustomButton("Back to Main Menu");
        backButton.setOnAction(e -> {stage.close();
            new MainMenuView(stage).show();});

        buttonSection.getChildren().addAll(saveButton, backButton);
        return buttonSection;
    }

    /**
     * Updates the preview section with the current lesson elements.
     */
    private void updatePreview() {
        VBox previewSection = (VBox) ((ScrollPane) ((VBox) ((BorderPane) stage.getScene().getRoot()).getCenter()).getChildren().get(0)).getContent();
        previewSection.getChildren().clear();

        // Add a title for the preview section
        Label previewTitle = createStyledLabel("Lesson Preview: " + nameField.getText(), 20, true, "black");
        previewSection.getChildren().add(previewTitle);

        // Retrieve the elements from the lesson
        List<Elements> lesson = lessonController.getLesson().getElements();

        // Sort the elements by position (ascending order)
        Collections.sort(lesson, new Comparator<Elements>() {
            @Override
            public int compare(Elements e1, Elements e2) {
                return e1.getPosition().compareTo(e2.getPosition());
            }
        });

        // Clear any existing elements in the preview section
        previewSection.getChildren().clear();

        // Loop through the sorted elements list and add them to the preview section
        for (int i = 0; i < lesson.size(); i++) {
            Elements element = lesson.get(i);

            System.out.println(lesson.get(i).getPosition());

            HBox elementBox = createElementBox(element, i);
            previewSection.getChildren().add(elementBox);
        }
    }

    /**
     * Displays an alert dialog with a message.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Creates a styled label with the specified text, font size, boldness, and color.
     * @param text The text to display.
     * @param fontSize The font size.
     * @param bold Whether the text should be bold.
     * @param color The text color.
     * @return A styled label with the specified properties.
     */
    private Label createStyledLabel(String text, int fontSize, boolean bold, String color) {
        Label label = new Label(text);
        String style = "-fx-font-size: " + fontSize + "px;";
        if (bold) {
            style += " -fx-font-weight: bold;";
        }
        label.setStyle(style + " -fx-text-fill:" + color + ";");
        return label;
    }

    // Element still in progress
    /**
     * Updates the input context based on the selected element type.
     * @param elementTypeComboBox The ComboBox for selecting element type.
     * @param inputContainer The container for input fields.
     */
    /**
     private void updateInputContextType(ComboBox<String> elementTypeComboBox, VBox inputContainer) {
     elementTypeComboBox.setOnAction(e -> {
     String selectedType = elementTypeComboBox.getValue();

     // Clear the current content
     inputContainer.getChildren().clear();

     // Label for content input
     Label contentLabel = createStyledLabel("Content:", 16, true, "white");

     // Add appropriate input field based on the selected type
     if ("Paragraph".equals(selectedType)) {
     TextArea contentArea = new TextArea();
     contentArea.setWrapText(true);
     contentArea.setPrefHeight(100); // Adjust height as needed
     inputContainer.getChildren().addAll(contentLabel, contentArea);

     // Example action to capture contentArea data
     contentArea.textProperty().addListener((observable, oldValue, newValue) -> {
     System.out.println("TextArea Content: " + newValue); // Handle changes as needed
     });
     } else if ("Sub-Title".equals(selectedType) || "Image".equals(selectedType) || "Video".equals(selectedType)) {
     TextField contentField = new TextField();
     inputContainer.getChildren().addAll(contentLabel, contentField);

     // Example action to capture contentField data
     contentField.textProperty().addListener((observable, oldValue, newValue) -> {
     System.out.println("TextField Content: " + newValue); // Handle changes as needed
     });
     }
     });
     }
     */

    /**
     * Updates the dynamic options based on the selected element type.
     * @param typeOptionBox The container for dynamic options.
     * @param elementTypeComboBox The ComboBox for selecting element type.
     * @param titleTypeComboBox The ComboBox for selecting title type.
     * @param paragraphTypeComboBox The ComboBox for selecting paragraph type.
     * @param contentField The TextField for entering content.
     */
    private void updateDynamicOptions(HBox typeOptionBox, ComboBox<String> elementTypeComboBox,
                                      ComboBox<TitleType> titleTypeComboBox, ComboBox<ParagraphType> paragraphTypeComboBox,
                                      TextField contentField) {

        String selectedType = elementTypeComboBox.getValue();
        typeOptionBox.getChildren().clear();

        // Add dynamic options based on the selected element type
        if ("Sub-Title".equals(selectedType)) {
            typeOptionBox.getChildren().add(titleTypeComboBox);
        } else if ("Paragraph".equals(selectedType)) {
            typeOptionBox.getChildren().add(paragraphTypeComboBox);
        } else if ("Image".equals(selectedType) || "Video".equals(selectedType)) {
            Button chooseFileButton = new CustomButton("Choose File");
            chooseFileButton.getStyleClass().add("button-blue");
            chooseFileButton.setOnAction(event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select a File");
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.mp4", "*.avi", "*.flv", "*.mov"));

                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    contentField.setText(selectedFile.getAbsolutePath());
                }
            });
            typeOptionBox.getChildren().add(chooseFileButton);
        }
    }

    /**
     * Adds an element to the lesson based on the selected element type.
     * @param contentField The TextField for entering content.
     * @param elementTypeComboBox The ComboBox for selecting element type.
     * @param titleTypeComboBox The ComboBox for selecting title type.
     * @param paragraphTypeComboBox The ComboBox for selecting paragraph type.
     * @throws IOException If an error occurs while reading the file.
     */
    private void addElement(TextField contentField, ComboBox<String> elementTypeComboBox,
                            ComboBox<TitleType> titleTypeComboBox, ComboBox<ParagraphType> paragraphTypeComboBox) throws IOException {
        String content = contentField.getText();
        String elementType = elementTypeComboBox.getValue();

        // Validate content input is not empty
        if (content.isEmpty()) {
            showAlert("Error", "Element content cannot be empty.");
            return;
        }

        // Validate content length based on element type
        int contentLength = content.length();
        if ("Sub-Title".equals(elementType) && contentLength > 250) { // Subtitle content limit is 250 characters
            int excess = contentLength - 250;
            showAlert("Error", "Subtitle content exceeds the limit by " + excess + " characters.");
            return;
        } else if ("Paragraph".equals(elementType) && contentLength > 1500) { // Paragraph content limit is 1500 characters
            int excess = contentLength - 1500;
            showAlert("Error", "Paragraph content exceeds the limit by " + excess + " characters.");
            return;
        } else if ("Video".equals(elementType)) { // Video content limit is 67108864 bytes (64 MB)
            byte[] videoData = LessonController.readAsBytes(content);
            int maxAllowedPacket = 67108864;

            if (videoData.length > maxAllowedPacket) {
                showAlert("Error", "The video exceeds the limit  of 64 MB.");
                return;
            }
        }


        // Add the element to the lesson
        switch (elementType) {
            case "Sub-Title":
                TitleType titleType = titleTypeComboBox.getValue();
                lessonController.createSubTitle(content, titleType);
                elementsList.add(new Title(content, titleType));
                break;
            case "Paragraph":
                ParagraphType paragraphType = paragraphTypeComboBox.getValue();
                lessonController.createParagraph(content, paragraphType);
                elementsList.add(new Paragraph(content, paragraphType));
                break;
            case "Image":
                try {
                    lessonController.createImage(content);
                } catch (IOException ex) {
                    showAlert("Error", "Failed to create image: " + ex.getMessage());
                }
                elementsList.add(new PictureIntegration(content));
                break;
            case "Video":
                lessonController.createVideo(content);
                elementsList.add(new VideoIntegration(content));
                break;
            default:
                showAlert("Error", "Unsupported element type.");
                break;
        }

        contentField.clear();
        updatePreview();
    }

    /**
     * Creates an HBox for displaying an element in the preview section.
     * @param element The element to display.
     * @param index The index of the element in the lesson.
     * @return An HBox containing the element and action buttons.
     */

    private HBox createElementBox(Elements element, int index) {
        HBox elementBox = new HBox(10);
        elementBox.setPadding(new Insets(10));
        elementBox.setAlignment(Pos.CENTER_LEFT);

        List<Elements> lesson = lessonController.getLesson().getElements();

        // Display the element content based on its type
        if (element instanceof Title) {
            Title title = (Title) element;
            Label titleLabel = new Label(title.getContent());
            titleLabel.setStyle(title.getType() == TitleType.MainTitle
                    ? "-fx-font-size: 20px; -fx-font-weight: bold;"
                    : "-fx-font-size: 16px; -fx-font-style: italic;");
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
                Label errorLabel = new Label("Image not available: " + image.getContentPath());
                errorLabel.setStyle("-fx-text-fill: red;");
                elementBox.getChildren().add(errorLabel);
            }
        } else if (element instanceof VideoIntegration) {
            VideoIntegration video = (VideoIntegration) element;
            try {
                Media media = new Media(new File(video.getContentPath()).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(media);
                MediaView mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth(300);
                mediaPlayer.play();
                mediaPlayer.pause();
                elementBox.getChildren().add(mediaView);
            } catch (Exception ex) {
                Label errorLabel = new Label("Video not available: " + video.getContentPath());
                errorLabel.setStyle("-fx-text-fill: red;");
                elementBox.getChildren().add(errorLabel);
            }
        }

        Button upButton = new Button("↑");
        upButton.setOnAction(event -> {
            if (index > 0) {
                lessonController.swapElements(lesson, index, index - 1, isUpdate);
                updatePreview();
            }
        });

        Button downButton = new Button("↓");
        downButton.setOnAction(event -> {
            if (index < lesson.size() - 1) {
                lessonController.swapElements(lesson, index, index + 1, isUpdate);
                updatePreview();
            }
        });

        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white;");
        deleteButton.setOnAction(event -> {
            lessonController.removeElement(element);
            updatePreview();
        });

        elementBox.getChildren().addAll(upButton, downButton, deleteButton);

        return elementBox;
    }

    /**
     * Saves the lesson to the database and returns to the main menu.
     */
    private void saveLesson() {
        String lessonName = nameField.getText();
        if (lessonController.validateInputs(lessonName)) {
            lessonController.createMainTitle(lessonName);
            lessonController.setTag(tagTypeComboBox.getValue());
            lessonController.createLesson();
            stage.close();
            new MainMenuView(stage).show();
        } else {
            showAlert("Error", "Please provide a valid lesson name.");
        }
    }

    /**
     * Updates the lesson in the database and returns to the main menu.
     */
    private void updateLesson() {

        if(!nameField.getText().isEmpty() && !Objects.equals(nameField.getText(), lessonController.getLesson().getTitle())) {
            lessonController.createMainTitle(nameField.getText());
        }

        if (tagTypeComboBox.getValue() != lessonController.getLesson().getTag()) {
            lessonController.setTag(tagTypeComboBox.getValue());
        }

        lessonController.updateLesson();
        stage.close();
        new MainMenuView(stage).show();
    }

}