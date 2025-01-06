package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LessonController;
import com.projet.da50.projet_da50.model.Lesson;
import com.projet.da50.projet_da50.model.Tags;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.LessonComponent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.util.List;

public class MainMenuView extends UI {
    private final Stage primaryStage;
    private final LessonController lessonController;
    private List<Lesson> lessonList;

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.lessonController = new LessonController();
        this.lessonList = lessonController.getAllLessons();
    }

    /**
     * Initializes and displays the main menu UI.
     */
    public void show() {
        GridPane grid = createMainLayout();

        // Add UI components
        addNavigationButtons(grid);
        addLogoutButton(grid);
        if (getAdmin()) {
            addAdminNavigationButtons(grid);
        }
        addSearchBar(grid);
        addLessons(grid);


        // Create and set the scene
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Creates the main layout for the view.
     * @return the configured GridPane.
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
     * Adds navigation buttons to the grid.
     * @param grid the GridPane to which the buttons are added.
     */
    private void addNavigationButtons(GridPane grid) {
        HBox navigationButtons = new HBox(20);
        navigationButtons.setAlignment(Pos.CENTER_LEFT);

        Button btnHome = createButton("Home", "button-blue", e -> { primaryStage.close();
            new MainMenuView(primaryStage).show();});
        Button btnQuizz = createButton("Quizz", "button-blue", e -> { primaryStage.close(); /* Navigate to Quizz view */ });

        Button btnWallet = createButton("Wallet", "button-blue", e -> { primaryStage.close();/* Handle Wallet action */ });

        navigationButtons.getChildren().addAll(btnHome, btnQuizz, btnWallet);

        grid.add(navigationButtons, 0, 0);
    }

    private void addLogoutButton(GridPane grid) {
        HBox logoutButton = new HBox(20);
        logoutButton.setAlignment(Pos.CENTER_RIGHT);

        CustomButton btnDisconnect = new CustomButton("Logout");
        btnDisconnect.getStyleClass().add("button-red");
        btnDisconnect.setOnAction(e -> {primaryStage.close();
            new AuthenticationFormView(primaryStage).show();});

        logoutButton.getChildren().add(btnDisconnect);

        grid.add(logoutButton, 1, 0);
    }

    /**
     * Adds admin navigation buttons to the grid.
     * @param grid the GridPane to which the buttons are added.
     */
    private void addAdminNavigationButtons(GridPane grid) {
        HBox navigationButtons = new HBox(20);
        navigationButtons.setAlignment(Pos.CENTER_LEFT);

        Button btnAdmin = createButton("Create Course", "button-blue", e -> {primaryStage.close();
            new CreateLessonView(primaryStage).show();});
        navigationButtons.getChildren().add(btnAdmin);

        grid.add(navigationButtons, 0, 1);
    }

    /**
     * Adds the search bar to the given grid.
     * @param grid the GridPane to which the search bar is added.
     */
    private void addSearchBar(GridPane grid) {
        HBox searchBar = new HBox(15);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Search by keyword...");
        searchField.setPrefWidth(300);

        ComboBox<Tags> categoryBox = new ComboBox<>(FXCollections.observableArrayList(
                Tags.ALL, Tags.BEGINNER, Tags.INTERMEDIATE, Tags.EXPERT
        ));
        categoryBox.getSelectionModel().selectFirst();

        Button searchButton = new Button("Search");
        searchButton.getStyleClass().add("button-blue");
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            Tags category = categoryBox.getValue();
            List<Lesson> filteredLessons = lessonController.findLesson(keyword, category);
            displayLessons(grid, filteredLessons);
        });

        searchBar.getChildren().addAll(searchField, categoryBox, searchButton);
        grid.add(searchBar, 0, 2);
    }

    /**
     * Displays the list of lessons in the grid.
     * @param grid the GridPane to which the lessons are added.
     */
    private void addLessons(GridPane grid) {
        displayLessons(grid, lessonList);
    }

    /**
     * Helper method to create a styled button with an action.
     * @param text the button text.
     * @param styleClass the CSS style class.
     * @param action the action to perform on button click.
     * @return the configured Button.
     */
    private Button createButton(String text, String styleClass, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(action);
        return button;
    }

    /**
     * Displays the given lessons in the grid.
     * @param grid the GridPane where lessons are displayed.
     * @param lessons the list of lessons to display.
     */
    private void displayLessons(GridPane grid, List<Lesson> lessons) {
        grid.getChildren().removeIf(node -> GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) >= 3);

        int rowIndex = 3; // Start just below the search bar
        for (Lesson lesson : lessons) {
            LessonComponent lessonComponent = new LessonComponent(primaryStage, lesson);
            GridPane.setColumnSpan(lessonComponent, GridPane.REMAINING);
            GridPane.setHgrow(lessonComponent, Priority.ALWAYS);
            grid.add(lessonComponent, 0, rowIndex++);
        }
    }
}
