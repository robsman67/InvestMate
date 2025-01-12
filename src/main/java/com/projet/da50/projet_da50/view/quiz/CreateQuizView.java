package com.projet.da50.projet_da50.view.quiz;

import com.projet.da50.projet_da50.controller.quiz.QuizController;
import com.projet.da50.projet_da50.model.quiz.Option;
import com.projet.da50.projet_da50.model.quiz.Question;
import com.projet.da50.projet_da50.view.MainMenuView;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.components.CustomButton;
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

/**
 * View for creating a new quiz.
 */
public class CreateQuizView extends UI {

    private Stage primaryStage;
    private QuizController controller;

    public CreateQuizView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new QuizController();
    }

    /**
     * Show the view.
     */
    public void show() {
        GridPane grid = createMainLayout();
        addNavigationButtons(grid);
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        // Root Layout for CreateQuizView
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));


        Label titleLabel = new Label("Choose Quiz Type:");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white ; -fx-font-weight: bold;");
        Button multiChoiceMultipleAnswersBtn = createButton("Multiple Choice Quiz",
                "button-blue",e -> showCreateQuestionView(primaryStage, true));
        Button multiChoiceSingleAnswerBtn = createButton("Single Choice Quiz",
                "button-blue",e -> showCreateQuestionView(primaryStage, false));
        root.getChildren().addAll(titleLabel, multiChoiceMultipleAnswersBtn, multiChoiceSingleAnswerBtn);
        grid.add(root,0,1);
        primaryStage.setTitle("Create quiz");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Show the view for creating questions.
     * @param stage The stage to show the view on.
     * @param isMultipleAnswers Whether the quiz allows multiple answers.
     */
    private void showCreateQuestionView(Stage stage, boolean isMultipleAnswers) {
        GridPane grid = createMainLayout();
        addNavigationButtons(grid);
        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        // Root Layout for CreateQuestionView
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Quiz Title Input
        Label quizTitleLabel = createStyledLabel("Quiz Title:", 14,true,"white");
        TextField quizTitleField = new TextField();
        quizTitleField.setPromptText("Enter the quiz title here");

        List<QuestionBox> questionBoxes = new ArrayList<>();

        Button addQuestionButton = new CustomButton("Add Question");
        Button saveQuizButton = new CustomButton("Save Quiz");

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

        root.getChildren().addAll(quizTitleLabel, quizTitleField, addQuestionButton, saveQuizButton);
        grid.add(root,0,1);
        stage.setScene(scene);
        stage.setMaximized(true);
    }

    /**
     * Create a button with the given text, style class and action.
     * @param text The text to display on the button.
     * @param styleClass The style class to apply to the button.
     * @param action The action to perform when the button is clicked.
     * @return The created button.
     */
    private Button createButton(String text, String styleClass, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        button.setOnAction(action);
        return button;
    }

    /**
     * Create a styled label with the given text, font size, boldness and color.
     * @param text The text to display on the label.
     * @param fontSize The font size of the label.
     * @param bold Whether the text should be bold.
     * @param color The color of the text.
     * @return The created label.
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
     * Create the main layout for the view.
     * @return The created layout.
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
     * Add navigation buttons to the view.
     * @param grid The grid to add the buttons to.
     */
    private void addNavigationButtons(GridPane grid) {
        HBox navigationButtons = new HBox(20);
        navigationButtons.setAlignment(Pos.CENTER_LEFT);

        Button btnHome = createButton("Home", "button-blue", e -> {
            primaryStage.close();
            new MainMenuView(primaryStage).show();
        });

        Button btnQuizz = createButton("Back", "button-blue", e -> {
            primaryStage.close();
            new ShowQuizView(primaryStage).show();
        });


        navigationButtons.getChildren().addAll(btnHome, btnQuizz);
        grid.add(navigationButtons, 0, 0);
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

        /**
         * Constructor for QuestionBox.
         * @param isMultipleAnswers Whether the question allows multiple answers.
         * @param questionBoxes The list of question boxes.
         */
        public QuestionBox(boolean isMultipleAnswers, List<QuestionBox> questionBoxes) {
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
        }

        /**
         * Add an option to the question.
         */
        private void addOption() {
            HBox optionRow = new HBox(10);
            TextField optionField = new TextField();
            optionField.setPromptText("Enter option text");
            CheckBox correctCheckBox = new CheckBox("Correct");
            correctCheckBox.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
            Button removeOptionButton = new CustomButton("Remove");

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

        /**
         * Delete the question.
         */
        private void deleteQuestion() {
            ((VBox) box.getParent()).getChildren().remove(box); // Remove from UI
            questionBoxes.remove(this); // Remove from list
        }

        /**
         * Convert the question box to a Question object.
         * @return The created Question object.
         */
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

        /**
         * Get the VBox containing the question box.
         * @return The VBox containing the question box.
         */
        public VBox getBox() {
            return box;
        }
    }
}