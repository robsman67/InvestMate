package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.quiz.QuizController;
import com.projet.da50.projet_da50.model.quiz.Quiz;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.components.QuizComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;


import java.util.List;

public class ShowQuizView extends UI {

    private QuizController controller;
    private Stage primaryStage;
    private List<Quiz> quizList;

    public ShowQuizView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
        this.quizList = controller.getAllQuizzes();
    }

    //    public void show() {
//        // Fetch data from database
//        List<Quiz> quizzes = this.controller.getAllQuizzes();
//
//        // Create a VBox to hold buttons
//        VBox vbox = new VBox(10);
//        vbox.setStyle("-fx-padding: 20px;");
//
//        // Create a button to redirect to CreateQuizView
//        Button btnCreateQuiz = new Button("Create Quiz");
//        btnCreateQuiz.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
//        btnCreateQuiz.setOnAction(e -> {
//            new CreateQuizView(primaryStage).show();
//        });
//        vbox.getChildren().add(btnCreateQuiz);
//
//        // Create a Label for the title
//        Label titleLabel = new Label("Available Quizzes");
//        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
//        vbox.getChildren().add(titleLabel);
//
//        // Create a Button for each quiz with numbering
//        int quizNumber = 1;
//        for (Quiz quiz : quizzes) {
//            HBox quizBox = new HBox(10);
//
//            Button quizButton = new Button(quizNumber + ". " + quiz.getTitle());
//            quizButton.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
//            quizButton.setOnAction(e -> {
//                new ShowQuestionView(primaryStage, quiz).show();
//            });
//
//            Button updateButton = new Button("Update");
//            updateButton.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
//            updateButton.setOnAction(e -> {
//                new UpdateQuizView(primaryStage, quiz.getId()).show();
//            });
//
//            quizBox.getChildren().addAll(quizButton, updateButton);
//            vbox.getChildren().add(quizBox);
//            quizNumber++;
//        }
//
//        // Create Main Menu button
//        Button btnMainMenu = new Button("Main Menu");
//        btnMainMenu.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
//        btnMainMenu.setOnAction(e -> {
//            new MainMenuView(primaryStage).show();
//        });
//
//        // Add the Main Menu button to the VBox
//        vbox.getChildren().add(btnMainMenu);
//
//        // Create the scene
//        Scene scene = new Scene(vbox, WINDOW_HEIGHT, WINDOW_WIDTH);
//
//        // Use custom UI class's show method
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Show Quiz View");
//        primaryStage.show();
//    }
    public void show() {
        GridPane grid = createMainLayout();
        addNavigationButtons(grid);
        if (getAdmin()) {
            addAdminNavigationButtons(grid);
        }
        addSearchBar(grid);
        addQuiz(grid);

        // Create and set the scene
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Quiz Menu");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    private GridPane createMainLayout() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.getStyleClass().add("main-background");

        ColumnConstraints column = new ColumnConstraints();
        column.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(column);

        return grid;
    }
    private Button createButton(String text, String styleClass, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(action);
        return button;
    }
    private void addNavigationButtons(GridPane grid) {
        HBox navigationButtons = new HBox(20);
        navigationButtons.setAlignment(Pos.CENTER_LEFT);

        Button btnHome = createButton("Home", "button-blue", e -> {
            primaryStage.close();
            new MainMenuView(primaryStage).show();
        });


        navigationButtons.getChildren().addAll(btnHome);
        grid.add(navigationButtons, 0, 0);
    }

    private void addAdminNavigationButtons(GridPane grid) {
        HBox adminButtons = new HBox(20);
        adminButtons.setAlignment(Pos.CENTER_LEFT);

        Button btnCreateCourse = createButton("Create Quiz", "button-blue", e -> {
            primaryStage.close();
            new CreateQuizView(primaryStage).show();
        });

        adminButtons.getChildren().add(btnCreateCourse);
        grid.add(adminButtons, 0, 1);
    }
    private void addQuiz(GridPane grid) {
        displayQuizzez(grid, quizList);
    }
    private void displayQuizzez(GridPane grid, List<Quiz> quizzez) {
        // Clear previous quizzez
        grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) >= 3);

        int rowIndex = 3;
        for (Quiz quiz : quizzez) {
            QuizComponent quizComponent = new QuizComponent(primaryStage, quiz, getAdmin());
            GridPane.setColumnSpan(quizComponent, GridPane.REMAINING);
            GridPane.setHgrow(quizComponent, Priority.ALWAYS);
            grid.add(quizComponent, 0, rowIndex++);
        }
    }

    /**
     * Adds a search bar to the grid.
     *
     * @param grid the {@link GridPane} to which the search bar is added.
     */
    private void addSearchBar(GridPane grid) {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword...");
        searchField.setPrefWidth(300);

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button-blue");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            List<Quiz> filteredQuizzes = controller.findQuizzesByTitle(keyword);
            displayFilteredQuizzez(grid, filteredQuizzes);
        });

        searchBar.getChildren().addAll(searchField, searchButton);
        grid.add(searchBar, 0, 2);
    }

    /**
     * Displays a list of filtered quizzes in the grid.
     *
     * @param grid the {@link GridPane} where lessons are displayed.
     * @param quizzes the list of {@link Quiz} objects to display.
     */
    private void displayFilteredQuizzez(GridPane grid, List<Quiz> quizzes) {
        // Clear previous lessons
        grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) >= 3);

        int rowIndex = 3; // Start below the search bar
        for (Quiz quiz : quizzes) {
            QuizComponent quizComponent = new QuizComponent(primaryStage, quiz, getAdmin());
            GridPane.setColumnSpan(quizComponent, GridPane.REMAINING);
            GridPane.setHgrow(quizComponent, Priority.ALWAYS);
            grid.add(quizComponent, 0, rowIndex++);
        }
    }


}