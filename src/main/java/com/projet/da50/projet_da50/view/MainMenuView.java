package com.projet.da50.projet_da50.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class MainMenuView extends UI {
    private Stage primaryStage;

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button createLessonButton = new Button("Créer une leçon");
        createLessonButton.setOnAction(e -> { new CreateLessonView(primaryStage).show(); });

        grid.add(createLessonButton, 0, 0);

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
