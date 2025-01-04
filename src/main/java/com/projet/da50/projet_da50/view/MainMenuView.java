package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LessonController;
import com.projet.da50.projet_da50.model.Lesson;
import com.projet.da50.projet_da50.model.Tags;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.LessonComponent;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainMenuView extends UI {
    private Stage primaryStage;
    private LessonController lessonController = new LessonController();
    List<Lesson> lessonList = new ArrayList<>();

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.lessonList = lessonController.getAllLessons();
    }

    public void show() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("main-background");

        // Étendre les colonnes sur toute la largeur
        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(column);

        // Barre de recherche : TextField, ComboBox et bouton
        HBox searchBar = new HBox(10); // Espacement entre les éléments
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // Zone de texte pour entrer les critères de recherche
        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword...");
        searchField.setPrefWidth(300); // Largeur de la zone de texte

        // Drop-down (ComboBox) pour sélectionner une catégorie
        ComboBox<Tags> categoryBox = new ComboBox<>(FXCollections.observableArrayList(
                Tags.ALL, Tags.BEGINNER, Tags.INTERMEDIATE, Tags.EXPERT
        ));
        categoryBox.getSelectionModel().selectFirst(); // Sélectionner la première option par défaut

        // Bouton de recherche
        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button-blue");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            Tags category = categoryBox.getValue();

            // Effectuer une recherche basée sur les critères
            List<Lesson> filteredLessons = lessonController.findLesson(keyword, category);
            displayLessons(grid, filteredLessons);
        });

        // Ajouter les éléments à la barre de recherche
        searchBar.getChildren().addAll(searchField, categoryBox, searchButton);

        // Ajouter la barre de recherche en haut de la grille
        grid.add(searchBar, 0, 1);

        // Ajouter les leçons dans les lignes suivantes
        displayLessons(grid, lessonList);

        // Ajouter le bouton "Home"
        Button btnHome = new Button("Home");
        btnHome.getStyleClass().add("button-blue");
        btnHome.setOnAction(e -> {
            // Rediriger vers la vue d'accueil ou le menu principal
            new MainMenuView(primaryStage).show();
        });
        grid.add(btnHome, 20, 0);

        // Ajouter le bouton "Quizz"
        Button btnQuizz = new Button("Quizz");
        btnQuizz.getStyleClass().add("button-blue");
        btnQuizz.setOnAction(e -> {
            // Rediriger vers la vue du quizz
        });
        grid.add(btnQuizz, 80, 0);

        // Bouton d'administration et autres boutons (si admin)
        if (getAdmin()) {
            Button btnAdmin = new Button("Create Course");
            btnAdmin.getStyleClass().add("button-blue");
            btnAdmin.setOnAction(e -> {
                new CreateLessonView(primaryStage).show();
            });
            grid.add(btnAdmin, 140, 0);
        }

        // Ajouter le bouton "Wallet"
        Button btnWallet = new Button("Wallet");
        btnWallet.getStyleClass().add("button-blue");
        btnWallet.setOnAction(e -> {
            // Handle Wallet button action (à implémenter plus tard)
        });
        grid.add(btnWallet, 20, 0);

        // Ajouter le bouton "Logout"
        CustomButton btnDisconnect = new CustomButton("Logout");
        btnDisconnect.getStyleClass().add("button-red");
        btnDisconnect.setOnAction(e -> {
            new AuthenticationFormView(primaryStage).show();
        });
        grid.add(btnDisconnect, 165, 0);

        // Créer la scène
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Méthode pour afficher les leçons filtrées dans la grille.
     */
    private void displayLessons(GridPane grid, List<Lesson> lessons) {
        // Supprimer les anciennes leçons affichées
        grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0);

        int rowIndex = 10;
        for (Lesson lesson : lessons) {
            LessonComponent lessonComponent = new LessonComponent(primaryStage, lesson);

            // Étendre le composant sur toute la ligne
            GridPane.setColumnSpan(lessonComponent, GridPane.REMAINING);
            GridPane.setHgrow(lessonComponent, Priority.ALWAYS);

            grid.add(lessonComponent, 0, rowIndex++);
        }
    }
}
