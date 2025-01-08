package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.QuizController;
import com.projet.da50.projet_da50.model.Option;
import com.projet.da50.projet_da50.model.Question;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.UI;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class CreateQuizView extends UI {

    private Stage primaryStage;
    private QuizController controller;

    public CreateQuizView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
    }

    public void show() {
        primaryStage.setTitle("Create Quiz");

        // Root Layout for CreateQuizView
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> {
            new MainMenuView(primaryStage).show();
        });

        Label titleLabel = new Label("Choose Quiz Type:");
        Button multiChoiceMultipleAnswersBtn = new Button("Multiple Choice (Multiple Answers)");
        Button multiChoiceSingleAnswerBtn = new Button("Multiple Choice (Single Answer)");

        multiChoiceMultipleAnswersBtn.setOnAction(e -> showCreateQuestionView(primaryStage, true));
        multiChoiceSingleAnswerBtn.setOnAction(e -> showCreateQuestionView(primaryStage, false));

        root.getChildren().addAll(btnMainMenu, titleLabel, multiChoiceMultipleAnswersBtn, multiChoiceSingleAnswerBtn);

        Scene scene = new Scene(root, WINDOW_HEIGHT, WINDOW_WIDTH);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showCreateQuestionView(Stage stage, boolean isMultipleAnswers) {
        // Root Layout for CreateQuestionView
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Create Main Menu button
        Button btnMainMenu = new Button("Main Menu");
        btnMainMenu.setOnAction(e -> {
            new MainMenuView(primaryStage).show();
        });

        // Quiz Title Input
        Label quizTitleLabel = new Label("Quiz Title:");
        TextField quizTitleField = new TextField();
        quizTitleField.setPromptText("Enter the quiz title here");

        List<QuestionBox> questionBoxes = new ArrayList<>();

        Button addQuestionButton = new Button("Add Question");
        Button saveQuizButton = new Button("Save Quiz");

        // Add question functionality
        addQuestionButton.setOnAction(e -> {
            QuestionBox questionBox = new QuestionBox(isMultipleAnswers, questionBoxes);
            questionBoxes.add(questionBox);
            root.getChildren().add(root.getChildren().size() - 2, questionBox.getBox());
        });

        // Save quiz functionality
        saveQuizButton.setOnAction(e -> {
            String quizTitle = quizTitleField.getText().trim();
            if (quizTitle.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Quiz title cannot be empty!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            List<Question> questions = new ArrayList<>();
            for (QuestionBox questionBox : questionBoxes) {
                questions.add(questionBox.toQuestion());
            }

            controller.createQuiz(quizTitle, isMultipleAnswers, questions);
            new ShowQuizView(primaryStage).show(); // Redirect to ShowQuizView after saving
        });

        root.getChildren().addAll(btnMainMenu, quizTitleLabel, quizTitleField, addQuestionButton, saveQuizButton);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setScene(scene);
    }

    // Helper class to manage question box
    class QuestionBox {
        private VBox box;
        private TextField questionField;
        private VBox optionsBox;
        private Button addOptionButton;
        private Button deleteQuestionButton;
        private boolean isMultipleAnswers;
        private List<QuestionBox> questionBoxes;

        public QuestionBox(boolean isMultipleAnswers, List<QuestionBox> questionBoxes) {
            this.isMultipleAnswers = isMultipleAnswers;
            this.questionBoxes = questionBoxes;

            box = new VBox(10);
            box.setPadding(new Insets(10));
            box.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

            questionField = new TextField();
            questionField.setPromptText("Enter your question here");

            optionsBox = new VBox(5);
            addOptionButton = new Button("Add Option");
            deleteQuestionButton = new Button("Delete Question");

            addOptionButton.setOnAction(e -> addOption());
            deleteQuestionButton.setOnAction(e -> deleteQuestion());

            box.getChildren().addAll(questionField, optionsBox, addOptionButton, deleteQuestionButton);
        }

        private void addOption() {
            HBox optionRow = new HBox(10);
            TextField optionField = new TextField();
            optionField.setPromptText("Enter option text");
            CheckBox correctCheckBox = new CheckBox("Correct");
            Button removeOptionButton = new Button("Remove");

            removeOptionButton.setOnAction(e -> optionsBox.getChildren().remove(optionRow));
            optionRow.getChildren().addAll(optionField, correctCheckBox, removeOptionButton);

            optionsBox.getChildren().add(optionRow);

            if (!isMultipleAnswers) {
                correctCheckBox.setOnAction(e -> {
                    if (correctCheckBox.isSelected()) {
                        for (var node : optionsBox.getChildren()) {
                            HBox otherRow = (HBox) node;
                            CheckBox otherCheckBox = (CheckBox) otherRow.getChildren().get(1);
                            if (otherCheckBox != correctCheckBox) {
                                otherCheckBox.setSelected(false);
                            }
                        }
                    }
                });
            }
        }

        private void deleteQuestion() {
            ((VBox) box.getParent()).getChildren().remove(box); // Remove from UI
            questionBoxes.remove(this); // Remove from list
        }

        public Question toQuestion() {
            List<Option> options = new ArrayList<>();
            for (var node : optionsBox.getChildren()) {
                HBox optionRow = (HBox) node;
                TextField optionField = (TextField) optionRow.getChildren().get(0);
                CheckBox correctCheckBox = (CheckBox) optionRow.getChildren().get(1);
                options.add(new Option(optionField.getText(), correctCheckBox.isSelected()));
            }
            return new Question(questionField.getText(), options);
        }

        public VBox getBox() {
            return box;
        }
    }
}