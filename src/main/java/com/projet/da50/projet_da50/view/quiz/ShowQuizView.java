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

    /**
     * Constructor for ShowQuizView.
     *
     * @param primaryStage The primary stage of the application.
     */
    public ShowQuizView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
        this.quizList = controller.getAllQuizzes();
    }


    /**
     * Displays the view.
     */
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

    /**
     * Creates the main layout of the view.
     * @return the main layout {@link GridPane}.
     */
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
    /**
     * Creates a button with the specified text, style class, and action.
     *
     * @param text the text of the button.
     * @param styleClass the style class of the button.
     * @param action the action to perform when the button is clicked.
     * @return the created button.
     */
    private Button createButton(String text, String styleClass, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(action);
        return button;
    }

    /**
     * Adds navigation buttons to the grid.
     *
     * @param grid the {@link GridPane} to which the buttons are added.
     */
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

    /**
     * Adds navigation buttons for admin users to the grid.
     *
     * @param grid the {@link GridPane} to which the buttons are added.
     */
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

    /**
     * Adds the list of quizzes to the grid.
     *
     * @param grid the {@link GridPane} to which the quizzes are added.
     */
    private void addQuiz(GridPane grid) {
        displayQuizzez(grid, quizList);
    }

    /**
     * Displays a list of quizzes in the grid.
     *
     * @param grid the {@link GridPane} where quizzes are displayed.
     * @param quizzez the list of {@link Quiz} objects to display.
     */
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