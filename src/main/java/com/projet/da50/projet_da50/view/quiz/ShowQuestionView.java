package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.OptionController;
import com.projet.da50.projet_da50.controller.QuestionController;
import com.projet.da50.projet_da50.model.Option;
import com.projet.da50.projet_da50.model.Question;
import com.projet.da50.projet_da50.model.Quiz;
import com.projet.da50.projet_da50.view.MainMenuView;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowQuestionView {
    private Stage primaryStage;
    private Quiz selectedQuiz;
    private QuestionController questionController;
    private OptionController optionController;
    private Map<Long, CheckBox> optionCheckBoxMap;

    public ShowQuestionView(Stage primaryStage, Quiz selectedQuiz) {
        this.primaryStage = primaryStage;
        this.selectedQuiz = selectedQuiz;
        this.questionController = new QuestionController();
        this.optionController = new OptionController();
        this.optionCheckBoxMap = new HashMap<>();
    }

    public void show() {
        VBox vbox = new VBox(15);

        Label quizTitle = new Label(selectedQuiz.getTitle());
        quizTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        vbox.getChildren().add(quizTitle);

        List<Question> questions = questionController.getAllQuestionsByQuizId(selectedQuiz.getId());
        int questionNumber = 1;

        for (Question question : questions) {
            Label questionLabel = new Label(questionNumber + ". " + question.getQuestion());
            questionLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px 0;");
            vbox.getChildren().add(questionLabel);

            List<Option> options = optionController.getAllOptionsByQuestionId(question.getId());
            VBox optionsBox = new VBox(10);
            Map<Long, CheckBox> questionOptionCheckBoxMap = new HashMap<>();

            for (Option option : options) {
                HBox optionBox = new HBox(10);
                CheckBox optionCheckBox = new CheckBox(option.getContent());
                questionOptionCheckBoxMap.put(option.getId(), optionCheckBox);
                optionCheckBoxMap.put(option.getId(), optionCheckBox);

                if (!selectedQuiz.getHasMultipleAnswers()) {
                    optionCheckBox.setOnAction(e -> {
                        if (optionCheckBox.isSelected()) {
                            for (CheckBox checkBox : questionOptionCheckBoxMap.values()) {
                                if (checkBox != optionCheckBox) {
                                    checkBox.setSelected(false);
                                }
                            }
                        }
                    });
                }

                optionBox.getChildren().add(optionCheckBox);
                optionsBox.getChildren().add(optionBox);
            }

            vbox.getChildren().add(optionsBox);
            questionNumber++;
        }

        HBox navigationBox = new HBox(10);

        Button btnBack = new Button("Back to Quizzes");
        btnBack.setOnAction(e -> new ShowQuizView(primaryStage).show());
        navigationBox.getChildren().add(btnBack);

        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> new MainMenuView(primaryStage).show());
        navigationBox.getChildren().add(btnMainMenu);

        Button btnSubmitQuiz = new Button("Submit Quiz");
        btnSubmitQuiz.setOnAction(e -> handleSubmitQuiz());
        navigationBox.getChildren().add(btnSubmitQuiz);

        vbox.getChildren().add(navigationBox);

        Scene scene = new Scene(vbox, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle(selectedQuiz.getTitle());
        primaryStage.show();
    }

    private void handleSubmitQuiz() {
        Map<Long, Boolean> userAnswers = new HashMap<>();

        for (Map.Entry<Long, CheckBox> entry : optionCheckBoxMap.entrySet()) {
            Long optionId = entry.getKey();
            CheckBox checkBox = entry.getValue();
            userAnswers.put(optionId, checkBox.isSelected());
        }

        if (!selectedQuiz.getHasMultipleAnswers()) {
            Map<Long, Boolean> singleAnswerUserAnswers = new HashMap<>();
            for (Question question : questionController.getAllQuestionsByQuizId(selectedQuiz.getId())) {
                for (Option option : optionController.getAllOptionsByQuestionId(question.getId())) {
                    if (userAnswers.getOrDefault(option.getId(), false)) {
                        singleAnswerUserAnswers.put(option.getId(), true);
                        break;
                    }
                }
            }
            userAnswers = singleAnswerUserAnswers;
        }

        showResults(userAnswers);
    }

    private void showResults(Map<Long, Boolean> userAnswers) {
        VBox vbox = new VBox(15);

        Label quizTitle = new Label(selectedQuiz.getTitle());
        quizTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        vbox.getChildren().add(quizTitle);

        List<Question> questions = questionController.getAllQuestionsByQuizId(selectedQuiz.getId());
        int questionNumber = 1;
        int totalQuestions = questions.size();
        int correctAnswers = 0;

        for (Question question : questions) {
            Label questionLabel = new Label(questionNumber + ". " + question.getQuestion());
            questionLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px 0;");
            vbox.getChildren().add(questionLabel);

            List<Option> options = optionController.getAllOptionsByQuestionId(question.getId());
            VBox optionsBox = new VBox(10);
            boolean isCorrect = true;

            for (Option option : options) {
                HBox optionBox = new HBox(10);
                CheckBox optionCheckBox = new CheckBox(option.getContent());
                optionCheckBox.setDisable(true);

                boolean userSelected = userAnswers.getOrDefault(option.getId(), false);
                boolean isOptionCorrect = option.isCorrect();

                if (userSelected) {
                    optionCheckBox.setSelected(true);
                    if (isOptionCorrect) {
                        optionCheckBox.setStyle("-fx-text-fill: green;");
                    } else {
                        optionCheckBox.setStyle("-fx-text-fill: red;");
                        isCorrect = false;
                    }
                } else if (isOptionCorrect) {
                    optionCheckBox.setStyle("-fx-text-fill: green;");
                    isCorrect = false;
                }

                optionBox.getChildren().add(optionCheckBox);
                optionsBox.getChildren().add(optionBox);
            }

            if (isCorrect) {
                correctAnswers++;
            }

            Label resultLabel = new Label(isCorrect ? "Correct" : "False");
            resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            optionsBox.getChildren().add(resultLabel);

            if (!isCorrect) {
                for (Option option : options) {
                    if (option.isCorrect()) {
                        Label correctAnswerLabel = new Label("The correct answer is: " + option.getContent());
                        correctAnswerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: blue;");
                        optionsBox.getChildren().add(correctAnswerLabel);
                    }
                }
            }

            vbox.getChildren().add(optionsBox);
            questionNumber++;
        }

        double score = ((double) correctAnswers / totalQuestions) * 100;
        Label scoreLabel = new Label("Your score: " + String.format("%.2f", score) + "%");
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vbox.getChildren().add(scoreLabel);

        Button btnBackToQuizzes = new Button("Back to Quizzes");
        btnBackToQuizzes.setOnAction(e -> new ShowQuizView(primaryStage).show());
        vbox.getChildren().add(btnBackToQuizzes);

        Scene scene = new Scene(vbox, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle(selectedQuiz.getTitle());
        primaryStage.show();
    }
}