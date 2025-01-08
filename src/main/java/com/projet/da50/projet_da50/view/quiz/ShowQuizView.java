package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.QuizController;
import com.projet.da50.projet_da50.model.Quiz;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.UI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class ShowQuizView extends UI {

    private QuizController controller;
    private Stage primaryStage;

    public ShowQuizView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
    }

    public void show() {
        // Fetch data from database
        List<Quiz> quizzes = this.controller.getAllQuizzes();

        // Create a VBox to hold buttons
        VBox vbox = new VBox(10);
        vbox.setStyle("-fx-padding: 20px;");

        // Check if the user is an admin
        boolean isAdmin = super.getAdmin();

        // Create a button to redirect to CreateQuizView if the user is an admin
        if (isAdmin) {
            Button btnCreateQuiz = new Button("Create Quiz");
            btnCreateQuiz.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
            btnCreateQuiz.setOnAction(e -> {
                new CreateQuizView(primaryStage).show();
            });
            vbox.getChildren().add(btnCreateQuiz);
        }

        // Create a Label for the title
        Label titleLabel = new Label("Available Quizzes");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        vbox.getChildren().add(titleLabel);

        // Create a Button for each quiz with numbering
        int quizNumber = 1;
        for (Quiz quiz : quizzes) {
            HBox quizBox = new HBox(10);

            Button quizButton = new Button(quizNumber + ". " + quiz.getTitle());
            quizButton.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
            quizButton.setOnAction(e -> {
                new ShowQuestionView(primaryStage, quiz).show();
            });

            quizBox.getChildren().add(quizButton);

            // Add update button if the user is an admin
            if (isAdmin) {
                Button updateButton = new Button("Update");
                updateButton.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
                updateButton.setOnAction(e -> {
                    new UpdateQuizView(primaryStage, quiz.getId()).show();
                });
                quizBox.getChildren().add(updateButton);
            }

            vbox.getChildren().add(quizBox);
            quizNumber++;
        }

        // Create Main Menu button
        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
        btnMainMenu.setOnAction(e -> {
            new MainMenuView(primaryStage).show();
        });

        // Add the Main Menu button to the VBox
        vbox.getChildren().add(btnMainMenu);

        // Create the scene
        Scene scene = new Scene(vbox, WINDOW_HEIGHT, WINDOW_WIDTH);

        // Use custom UI class's show method
        primaryStage.setScene(scene);
        primaryStage.setTitle("Show Quiz View");
        primaryStage.show();
    }

}