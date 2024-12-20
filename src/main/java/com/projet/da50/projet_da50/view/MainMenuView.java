package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.view.components.CustomButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainMenuView extends UI {
    private Stage primaryStage;

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("main-background");

        // Add Courses button
        Button btnCourses = new Button("Courses");
        btnCourses.getStyleClass().add("button-blue");
        btnCourses.setOnAction(e -> {
            // Handle Courses button action (à implémenter plus tard)
        });
        grid.add(btnCourses, 0, 0);

        // Add Wallet button
        Button btnWallet = new Button("Wallet");
        btnWallet.getStyleClass().add("button-blue");
        btnWallet.setOnAction(e -> {
            // Handle Wallet button action (à implémenter plus tard)
        });
        grid.add(btnWallet, 0, 1);

        // Add Disconnect button
        CustomButton btnDisconnect = new CustomButton("Logout");
        btnDisconnect.getStyleClass().add("button-red");
        btnDisconnect.setOnAction(e -> {
            new AuthenticationFormView(primaryStage).show();
        });
        grid.add(btnDisconnect, 0, 2);

        // Request focus on another element to prevent the Disconnect button from being selected
        btnCourses.requestFocus();

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}