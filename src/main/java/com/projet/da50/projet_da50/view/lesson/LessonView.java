package com.projet.da50.projet_da50.view.lesson;

import com.projet.da50.projet_da50.controller.lesson.LessonController;
import com.projet.da50.projet_da50.model.lesson.*;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.UI;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The LessonView class is responsible for displaying a lesson's content, including titles,
 * paragraphs, images, and videos, in a user-friendly interface using JavaFX.
 */
public class LessonView extends UI {

    private final Stage primaryStage;
    private final LessonController lessonController;
    private final String title;

    /**
     * Constructor for LessonView.
     *
     * @param primaryStage the primary stage for this view
     * @param title        the title of the lesson to display
     */
    public LessonView(Stage primaryStage, String title) {
        this.primaryStage = primaryStage;
        this.lessonController = new LessonController();
        this.title = title;
    }

    /**
     * Displays the lesson view.
     */
    public void show() {
        // Main container for the scene
        BorderPane root = createMainLayout();

        // Fetch the lesson based on the title
        Lesson lesson = lessonController.getLessonByTitle(title);
        if (lesson == null) {
            showError(root, "Lesson not found.");
            return;
        }

        // Add navigation buttons to the top
        addNavigationButtons(root, lesson);

        // Add lesson elements to the layout
        populateLessonElements(root, lesson);

        // Create and set up the scene
        setupAndShowScene(root);
    }

    /**
     * Creates the main layout for the scene.
     *
     * @return a configured BorderPane instance
     */
    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.getStyleClass().add("main-background");
        return root;
    }

    /**
     * Displays an error message in the UI.
     *
     * @param root  the root layout of the scene
     * @param error the error message to display
     */
    private void showError(BorderPane root, String error) {
        Label errorLabel = new Label(error);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
        root.setCenter(errorLabel);
    }

    /**
     * Adds navigation buttons to the top layout.
     *
     * @param root the root layout of the scene
     */
    private void addNavigationButtons(BorderPane root, Lesson lesson) {
        HBox navigationBar = new HBox(15);
        navigationBar.setAlignment(Pos.TOP_LEFT);
        navigationBar.setPadding(new Insets(20));

        Button homeButton = new Button("Home");
        homeButton.getStyleClass().add("button-blue");
        homeButton.setOnAction(event -> {
            primaryStage.close();
            new MainMenuView(primaryStage).show();
        });

        /**
        Button shareButton = new Button("Share");
        shareButton.getStyleClass().add("button-blue");
        shareButton.setOnAction(event -> {
            // Action for Share button
        });
        */

        navigationBar.getChildren().addAll(homeButton);

        if (getAdmin()) {
            Button modifyButton = new Button("Modify");
            modifyButton.getStyleClass().add("button-blue");
            modifyButton.setOnAction(event -> {
                primaryStage.close();
                new CreateLessonView(primaryStage, lesson).show();
            });
            navigationBar.getChildren().addAll(modifyButton);
        }

        root.setTop(navigationBar);
    }

    /**
     * Populates the lesson content into the UI layout.
     *
     * @param root   the root layout of the scene
     * @param lesson the lesson containing elements to display
     */
    private void populateLessonElements(BorderPane root, Lesson lesson) {
        VBox section = new VBox(20);
        section.setAlignment(Pos.TOP_CENTER); // Center horizontally and align to the top
        section.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(section);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);

        Label mainTitle = createMainTitle();
        section.getChildren().add(mainTitle);

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #000000; -fx-arc-height: 5");
        section.getChildren().add(separator);

        List<Elements> elementsList = lesson.getElements();;

        // Sort the elements by position (ascending order)
        Collections.sort(elementsList, new Comparator<Elements>() {
            @Override
            public int compare(Elements e1, Elements e2) {
                return e1.getPosition().compareTo(e2.getPosition());
            }
        });

        // Loop through the sorted elements list and add them to the preview section
        for (int i = 0; i < elementsList.size(); i++) {
            Elements element = elementsList.get(i);

            System.out.println(elementsList.get(i).getPosition());

            HBox elementBox = createElementBox(element);
            section.getChildren().add(elementBox);
        }
    }

    /**
     * Creates the main title label for the lesson.
     *
     * @return a styled Label instance
     */
    private Label createMainTitle() {
        Label mainTitle = new Label(title);
        mainTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        return mainTitle;
    }

    /**
     * Creates a UI box for a specific lesson element.
     *
     * @param element the lesson element to render
     * @return an HBox containing the rendered element
     */
    private HBox createElementBox(Elements element) {
        HBox elementBox = new HBox(10);
        elementBox.setAlignment(Pos.CENTER); // Center elements in the HBox

        if (element instanceof Title) {
            renderTitleElement((Title) element, elementBox);
        } else if (element instanceof Paragraph) {
            renderParagraphElement((Paragraph) element, elementBox);
        } else if (element instanceof PictureIntegration) {
            renderPictureElement((PictureIntegration) element, elementBox);
        } else if (element instanceof VideoIntegration) {
            renderVideoElement((VideoIntegration) element, elementBox);
        }

        return elementBox;
    }

    /**
     * Renders a title element.
     *
     * @param title      the Title element to render
     * @param elementBox the HBox to add the rendered element to
     */
    private void renderTitleElement(Title title, HBox elementBox) {
        Label titleLabel = new Label(title.getContent());
        titleLabel.setStyle(
                title.getType() == TitleType.MainTitle
                        ? "-fx-font-size: 24px; -fx-font-weight: bold;"
                        : "-fx-font-size: 16px; -fx-font-style: italic;"
        );
        elementBox.getChildren().add(titleLabel);
    }

    /**
     * Renders a paragraph element.
     *
     * @param paragraph  the Paragraph element to render
     * @param elementBox the HBox to add the rendered element to
     */
    private void renderParagraphElement(Paragraph paragraph, HBox elementBox) {
        Label paragraphLabel = new Label(paragraph.getContent());
        paragraphLabel.setStyle("-fx-font-size: 14px;");
        paragraphLabel.setWrapText(true);
        elementBox.getChildren().add(paragraphLabel);
    }

    /**
     * Renders a picture element.
     *
     * @param image      the PictureIntegration element to render
     * @param elementBox the HBox to add the rendered element to
     */
    private void renderPictureElement(PictureIntegration image, HBox elementBox) {
        byte[] imageData = image.getImageData(); // Assuming image.getContentData() returns the byte[]
        try {
            // Create an InputStream from the byte array
            InputStream inputStream = new ByteArrayInputStream(imageData);

            // Create a JavaFX Image from the InputStream
            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(inputStream);

            // Create an ImageView for the Image
            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);

            // Add the ImageView to the elementBox
            elementBox.getChildren().add(imageView);
        } catch (Exception ex) {
            Label errorLabel = new Label("Image not available");
            errorLabel.setStyle("-fx-text-fill: red;");
            elementBox.getChildren().add(errorLabel);
            ex.printStackTrace();
        }
    }

    /**
     * Renders a video element.
     *
     * @param video      the VideoIntegration element to render
     * @param elementBox the HBox to add the rendered element to
     */
    private void renderVideoElement(VideoIntegration video, HBox elementBox) {
        byte[] videoData = video.getVideoData(); // Assuming video.getContentData() returns the byte[]
        try {
            // Write the byte[] data to a temporary file
            File tempFile = File.createTempFile("video", ".mp4");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(videoData);
            }

            // Create a Media object from the temporary file
            Media media = new Media(tempFile.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(300);

            // Create play and stop buttons
            Button playButton = new Button("Play");
            playButton.getStyleClass().add("button-blue");

            // Set button actions
            playButton.setOnAction(e -> {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                    playButton.setText("Play");
                } else {
                    mediaPlayer.play();
                    playButton.setText("Pause");
                }
            });

            // Arrange MediaView and buttons in a VBox
            VBox videoControls = new VBox(5, mediaView, playButton);
            videoControls.setAlignment(Pos.CENTER);

            // Add the video controls to the elementBox
            elementBox.getChildren().add(videoControls);

            // Delete the temporary file on JVM exit
            tempFile.deleteOnExit();
        } catch (Exception ex) {
            Label errorLabel = new Label("Video not available");
            errorLabel.setStyle("-fx-text-fill: red;");
            elementBox.getChildren().add(errorLabel);
            ex.printStackTrace();
        }
    }

    /**
     * Sets up and displays the scene.
     *
     * @param root the root layout of the scene
     */
    private void setupAndShowScene(BorderPane root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}