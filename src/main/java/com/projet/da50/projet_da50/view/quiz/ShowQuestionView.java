package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.quiz.OptionController;
import com.projet.da50.projet_da50.controller.quiz.QuestionController;
import com.projet.da50.projet_da50.model.quiz.Option;
import com.projet.da50.projet_da50.model.quiz.Question;
import com.projet.da50.projet_da50.model.quiz.Quiz;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowQuestionView extends UI {
    private Stage primaryStage;
    private Quiz selectedQuiz;
    private QuestionController questionController;
    private OptionController optionController;
    private Map<Long, CheckBox> optionCheckBoxMap;

    /**
     * Constructor for ShowQuestionView.
     *
     * @param primaryStage The primary stage of the application.
     * @param selectedQuiz The quiz to show the questions for.
     */
    public ShowQuestionView(Stage primaryStage, Quiz selectedQuiz) {
        this.primaryStage = primaryStage;
        this.selectedQuiz = selectedQuiz;
        this.questionController = new QuestionController();
        this.optionController = new OptionController();
        this.optionCheckBoxMap = new HashMap<>();
    }

    /**
     * Show the view.
     */
    public void show() {
        GridPane grid = createMainLayout();
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        VBox vbox = new VBox(15);
        Button btnBack = new CustomButton("Back to Quizzes");
        btnBack.setOnAction(e -> new ShowQuizView(primaryStage).show());
        vbox.getChildren().add(btnBack);
        Label quizTitle = createStyledLabel(selectedQuiz.getTitle(),20,true,"white");
        vbox.getChildren().add(quizTitle);

        List<Question> questions = questionController.getAllQuestionsByQuizId(selectedQuiz.getId());
        int questionNumber = 1;

        for (Question question : questions) {
            Label questionLabel = new CustomLabel(questionNumber + ". " + question.getQuestion());
//            questionLabel.setStyle("-fx-font-size: 18px; -fx-padding: 10px 0;");
            vbox.getChildren().add(questionLabel);

            List<Option> options = optionController.getAllOptionsByQuestionId(question.getId());
            VBox optionsBox = new VBox(10);
            Map<Long, CheckBox> questionOptionCheckBoxMap = new HashMap<>();

            for (Option option : options) {
                HBox optionBox = new HBox(10);
                CheckBox optionCheckBox = new CheckBox(option.getContent());
                optionCheckBox.setStyle("-fx-text-fill: white;");
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


        Button btnSubmitQuiz = new CustomButton("Submit Quiz");
        btnSubmitQuiz.setOnAction(e -> handleSubmitQuiz());
        navigationBox.getChildren().add(btnSubmitQuiz);

        vbox.getChildren().add(navigationBox);
        grid.add(vbox,0,0);

        primaryStage.setTitle(selectedQuiz.getTitle());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Create the main layout.
     *
     * @return The main layout.
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
     * Create a styled label.
     *
     * @param text The text of the label.
     * @param fontSize The font size of the label.
     * @param bold Whether the label should be bold.
     * @param color The color of the label.
     * @return The styled label.
     */
    private Label createStyledLabel(String text, int fontSize, boolean bold, String color) {
        Label label = new Label(text);
        String style = "-fx-font-size: " + fontSize + "px;";
        if (bold) {
            style += " -fx-font-weight: bold;";
        }
        label.setStyle(style + " -fx-text-fill:" + color + ";");
        return label;
    }

    /**
     * Handle the submission of the quiz.
     */
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

    /**
     * Show the results of the quiz.
     * @param userAnswers The answers given by the user.
     */
    private void showResults(Map<Long, Boolean> userAnswers) {
        GridPane grid = createMainLayout();
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        VBox vbox = new VBox(15);

        Label quizTitle = new CustomLabel(selectedQuiz.getTitle());
        quizTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        vbox.getChildren().add(quizTitle);

        List<Question> questions = questionController.getAllQuestionsByQuizId(selectedQuiz.getId());
        int questionNumber = 1;
        int totalQuestions = questions.size();
        int correctAnswers = 0;

        for (Question question : questions) {
            Label questionLabel = new CustomLabel(questionNumber + ". " + question.getQuestion());
            questionLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10px 0;");
            vbox.getChildren().add(questionLabel);

            List<Option> options = optionController.getAllOptionsByQuestionId(question.getId());
            VBox optionsBox = new VBox(10);
            boolean isCorrect = true;

            for (Option option : options) {
                HBox optionBox = new HBox(10);
                CheckBox optionCheckBox = new CheckBox(option.getContent());
                optionCheckBox.setDisable(true);
                optionCheckBox.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");


                boolean userSelected = userAnswers.getOrDefault(option.getId(), false);
                boolean isOptionCorrect = option.isCorrect();

                if (userSelected) {
                    optionCheckBox.setSelected(true);
                    if (isOptionCorrect) {
                        optionCheckBox.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    } else {
                        optionCheckBox.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
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

            Label resultLabel = new CustomLabel(isCorrect ? "Correct" : "False");
            resultLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            optionsBox.getChildren().add(resultLabel);

            if (!isCorrect) {
                for (Option option : options) {
                    if (option.isCorrect()) {
                        Label correctAnswerLabel = new CustomLabel("The correct answer is: " + option.getContent());
                        correctAnswerLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: blue;");
                        optionsBox.getChildren().add(correctAnswerLabel);
                    }
                }
            }

            vbox.getChildren().add(optionsBox);
            questionNumber++;
        }

        double score = ((double) correctAnswers / totalQuestions) * 100;
        Label scoreLabel = new CustomLabel("Your score: " + String.format("%.2f", score) + "%");
        scoreLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        vbox.getChildren().add(scoreLabel);

        Button btnBackToQuizzes = new CustomButton("Back to Quizzes");
        btnBackToQuizzes.setOnAction(e -> new ShowQuizView(primaryStage).show());
        vbox.getChildren().add(btnBackToQuizzes);

        grid.add(vbox,0,0);
        primaryStage.setTitle(selectedQuiz.getTitle());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}