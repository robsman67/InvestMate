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

/**
 * This class represents a custom component for displaying lesson information in an HBox layout.
 */
public class LessonComponent extends HBox {

    /**
     * Constructor for LessonComponent.
     * Initializes the component with the given primary stage and lesson details.
     *
     * @param primaryStage The primary stage of the application.
     * @param lesson The lesson object containing the lesson details.
     */
    public LessonComponent(Stage primaryStage, Lesson lesson) {
        // Force the component to extend to the full width
        setPrefWidth(Double.MAX_VALUE);
        setMaxWidth(Double.MAX_VALUE);

        // Basic configuration of the HBox
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setPadding(new Insets(10));
        setStyle(
                "-fx-background-color: #f5f5f5; " + // Light background color
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #d1d1d1; " + // Border color
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px;"
        );

        // Create labels for the content
        Label nameLabel = new Label("Title: " + lesson.getTitle());
        nameLabel.setFont(Font.font(16));
        nameLabel.setTextFill(Color.BLACK);

        Label tagLabel = new Label("Tag: " + lesson.getTag());
        tagLabel.setFont(Font.font(14));
        tagLabel.setTextFill(Color.GRAY);

        // Flexible spacing to balance the contents
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Create the "Read" button on the right
        Button readButton = new Button("Read");
        readButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        readButton.setOnAction(event -> {
            System.out.println("Clicked on " + lesson.getTitle());
            primaryStage.close();
            new LessonView(primaryStage, lesson.getTitle()).show();
        });

        // Add elements to the HBox
        getChildren().addAll(nameLabel, tagLabel, spacer, readButton);
    }
}