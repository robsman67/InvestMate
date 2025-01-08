package com.projet.da50.projet_da50.view.components;
import com.projet.da50.projet_da50.controller.QuizController;
import com.projet.da50.projet_da50.model.Quiz;
import com.projet.da50.projet_da50.view.quiz.*;
import com.projet.da50.projet_da50.view.MainMenuView;
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
public class QuizComponent extends HBox{
    public QuizComponent(Stage primaryStage, Quiz quiz, boolean isAdmin) {

        // Configuration de base du HBox
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(20);
        setPadding(new Insets(10));
        setStyle(
                "-fx-background-color: #f5f5f5; " + // Couleur de fond clair
                        "-fx-background-radius: 5px; " +
                        "-fx-border-color: #d1d1d1; " + // Couleur du cadre
                        "-fx-border-width: 2px; " +
                        "-fx-border-radius: 5px;"
        );

        // Création des labels pour le contenu
        Label nameLabel = new Label("Title: " + quiz.getTitle());
        nameLabel.setFont(Font.font(16));
        nameLabel.setTextFill(Color.BLACK);
        System.out.println(nameLabel);

        // Espacement flexible pour équilibrer les contenus
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Création du bouton "Lire" à droite
        Button readButton = new Button("Show quiz");
        readButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
        readButton.setOnAction(event -> {
            primaryStage.close();
            new ShowQuestionView(primaryStage, quiz).show();
        });

        // Ajout des éléments au HBox
        getChildren().addAll(nameLabel, spacer,readButton);

        if(isAdmin){
            Button suppBtn = new Button("X");
            suppBtn.setStyle("-fx-background-color: Red; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");
            suppBtn.setOnAction(event -> {
                QuizController quizController = new QuizController();
                quizController.deleteQuiz(quiz.getId());

                primaryStage.close();
                new ShowQuizView(primaryStage).show();
            });

            // Ajout des éléments au HBox
            getChildren().addAll(suppBtn);
        }
    }
}
