package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.OptionController;
import com.projet.da50.projet_da50.controller.QuestionController;
import com.projet.da50.projet_da50.controller.QuizController;
import com.projet.da50.projet_da50.model.Option;
import com.projet.da50.projet_da50.model.Question;
import com.projet.da50.projet_da50.model.Quiz;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class UpdateQuizView extends UI {

    private Stage primaryStage;
    private QuizController controller;
    private QuestionController questionController;
    private OptionController optionController;
    private long quizId; // Quiz identifier for fetching/updating data

    public UpdateQuizView(Stage primaryStage, long quizId) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
        this.quizId = quizId;
        this.questionController = new QuestionController();
        this.optionController = new OptionController();
    }

    public void show() {
        GridPane grid = createMainLayout();
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Root Layout for UpdateQuizView
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Button btnBack = createButton("Back to quiz list", "button-blue",
        e -> {
            new ShowQuizView(primaryStage).show();
        });

        Label titleLabel = new CustomLabel("Quiz Title:");
        TextField quizTitleField = new TextField();

        List<QuestionBox> questionBoxes = new ArrayList<>();

        Button addQuestionButton = new CustomButton("Add Question");
        Button updateQuizButton = new CustomButton("Update Quiz");

        // Add question functionality
        addQuestionButton.setOnAction(e -> {
            QuestionBox questionBox = new QuestionBox(true, questionBoxes); // Assume multiple answers for simplicity
            questionBoxes.add(questionBox);
            root.getChildren().add(root.getChildren().size() - 2, questionBox.getBox());
        });

        // Update quiz functionality
        updateQuizButton.setOnAction(e -> {
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

            Quiz newQuiz = new Quiz();
            newQuiz.setId(quizId); // Ensure the correct quiz is updated
            newQuiz.setTitle(quizTitle);
            newQuiz.setHasMultipleAnswers(controller.getQuizById(quizId).getHasMultipleAnswers());

            controller.updateQuiz(newQuiz, questions);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quiz updated successfully!", ButtonType.OK);
            alert.showAndWait();
        });

        root.getChildren().addAll(btnBack, titleLabel, quizTitleField, addQuestionButton, updateQuizButton);
        grid.add(root,0,0);
        // Fetch quiz data and populate the view
        fetchAndPopulateQuizData(quizTitleField, questionBoxes, root);

        primaryStage.setTitle("Update quiz");
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

    private void fetchAndPopulateQuizData(TextField quizTitleField, List<QuestionBox> questionBoxes, VBox root) {
        // Fetch quiz data from the controller
        var quiz = controller.getQuizById(quizId);
        if (quiz == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to load quiz data.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Populate title
        quizTitleField.setText(quiz.getTitle());

        // Populate questions
        List<Question> questions = questionController.getAllQuestionsByQuizId(quizId);
        for (Question question : questions) {
            QuestionBox questionBox = new QuestionBox(quiz.getHasMultipleAnswers(), questionBoxes, question);
            questionBoxes.add(questionBox);
            root.getChildren().add(root.getChildren().size() - 2, questionBox.getBox());
        }
    }

    // Modified QuestionBox class to accept existing data
    class QuestionBox {
        private VBox box;
        private TextField questionField;
        private VBox optionsBox;
        private Button addOptionButton;
        private Button deleteQuestionButton;
        private boolean isMultipleAnswers;
        private List<QuestionBox> questionBoxes;

        public QuestionBox(boolean isMultipleAnswers, List<QuestionBox> questionBoxes) {
            this(isMultipleAnswers, questionBoxes, null);
        }

        public QuestionBox(boolean isMultipleAnswers, List<QuestionBox> questionBoxes, Question question) {
            this.isMultipleAnswers = isMultipleAnswers;
            this.questionBoxes = questionBoxes;

            box = new VBox(10);
            box.setPadding(new Insets(10));
            box.setStyle("-fx-border-color: white; -fx-border-width: 1; -fx-padding: 10;");

            questionField = new TextField();
            questionField.setPromptText("Enter your question here");

            optionsBox = new VBox(5);
            addOptionButton = new CustomButton("Add Option");
            deleteQuestionButton = new CustomButton("Delete Question");

            addOptionButton.setOnAction(e -> addOption());
            deleteQuestionButton.setOnAction(e -> deleteQuestion());

            box.getChildren().addAll(questionField, optionsBox, addOptionButton, deleteQuestionButton);

            // Populate with existing data if available
            if (question != null) {
                questionField.setText(question.getQuestion());
                List<Option> options = optionController.getAllOptionsByQuestionId(question.getId());
                for (Option option : options) {
                    addOption(option);
                }
            }
        }

        private void addOption() {
            addOption(null);
        }

        private void addOption(Option option) {
            HBox optionRow = new HBox(10);
            TextField optionField = new TextField();
            optionField.setPromptText("Enter option text");
            CheckBox correctCheckBox = new CheckBox("Correct");
            correctCheckBox.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
            Button removeOptionButton = new CustomButton("Remove");

            if (option != null) {
                optionField.setText(option.getContent());
                correctCheckBox.setSelected(option.isCorrect());
            }

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
