package com.projet.da50.projet_da50.view.authentification;

import com.projet.da50.projet_da50.controller.authentification.ErrorHandler;
import com.projet.da50.projet_da50.controller.LogController;
import com.projet.da50.projet_da50.controller.user.UserController;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import com.projet.da50.projet_da50.view.components.CustomPasswordField;
import com.projet.da50.projet_da50.view.components.CustomTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * This class represents the view for creating a new account.
 * It extends the UI class and provides the user interface for account creation.
 */
public class CreateAccountFormView extends UI {

    private final UserController userController = new UserController();
    private final Stage primaryStage;
    private final ErrorHandler errorHandler;
    private Label errorLabel;
    private final LogController logController = new LogController();

    /**
     * Constructor for CreateAccountFormView.
     *
     * @param primaryStage The primary stage for this view.
     */
    public CreateAccountFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.errorHandler = new ErrorHandler();
    }

    /**
     * Displays the account creation form.
     */
    @Override
    public void show() {
        primaryStage.setTitle("Create Account");

        // Create the main GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Create a rectangle for the background gradient
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#243447")), // Start color
                new Stop(1, Color.web("#0d1117"))  // End color
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(gradient);

        // Apply Gaussian blur effect to the background
        GaussianBlur blur = new GaussianBlur(20);
        background.setEffect(blur);

        // Create a StackPane to overlay the background and the content
        StackPane root = new StackPane();
        root.getChildren().addAll(background, grid);

        // Create the form GridPane
        GridPane formGrid = new GridPane(); // Container for form fields
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(30));
        formGrid.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        formGrid.setMaxWidth(WINDOW_WIDTH * 0.4);
        formGrid.setMaxHeight(WINDOW_HEIGHT * 0.5);

        // Username Field
        CustomLabel userLabel = new CustomLabel("Username:");
        CustomTextField userField = new CustomTextField();
        formGrid.add(userLabel, 0, 0);
        formGrid.add(userField, 1, 0);

        // Mail Field
        CustomLabel mailLabel = new CustomLabel("Mail:");
        CustomTextField mailField = new CustomTextField();
        formGrid.add(mailLabel, 0, 1);
        formGrid.add(mailField, 1, 1);

        // Password Field
        CustomLabel pwLabel = new CustomLabel("Password:");
        CustomPasswordField pwField = new CustomPasswordField();
        formGrid.add(pwLabel, 0, 2);
        formGrid.add(pwField, 1, 2);

        // Error Label (hidden by default)
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        formGrid.add(errorLabel, 0, 3, 2, 1); // Span across two columns

        // Create Account Button
        CustomButton btnCreateAccount = new CustomButton("Create Account");
        btnCreateAccount.getStyleClass().add("button-lightblue");
        btnCreateAccount.setDefaultButton(true);
        btnCreateAccount.setOnAction(e -> handleCreateAccount(userField, pwField, mailField, formGrid));

        // Back Button
        CustomButton btnBack = new CustomButton("Go back");
        btnBack.setOnAction(e -> {
            new AuthenticationFormView(primaryStage).show();
        });

        // Buttons HBox
        HBox buttonsBox = new HBox(10, btnBack, btnCreateAccount);
        buttonsBox.setAlignment(Pos.CENTER);
        formGrid.add(buttonsBox, 0, 4, 2, 1); // Span across two columns

        // Add the formGrid to the main GridPane
        grid.add(formGrid, 0, 0);

        // Create and set the scene
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Handles the account creation process.
     *
     * @param userField The text field for the username.
     * @param pwField The password field for the password.
     * @param mailField The text field for the email.
     * @param formGrid The grid pane containing the form fields.
     */
    private void handleCreateAccount(CustomTextField userField, CustomPasswordField pwField, CustomTextField mailField, GridPane formGrid) {
        String username = userField.getText();
        String password = pwField.getText();
        String mail = mailField.getText();

        // Remove the error label if it is visible
        formGrid.getChildren().remove(errorLabel);

        String validationMessage = errorHandler.validateCreateAccountFields(username, password, mail);
        if ("Valid credentials.".equals(validationMessage)) {
            userController.createUser(username, password, mail);
            logController.createLog(userController.findUserByUsername(username).getId(), "Account created", "");
            new AuthenticationFormView(primaryStage).show();
        } else {
            errorLabel.setText(validationMessage);
            errorLabel.setVisible(true);
            formGrid.add(errorLabel, 0, 3, 2, 1); // Span across two columns

            if (validationMessage.contains("Password")) {
                pwField.clear();
                pwField.requestFocus();
            } else if (validationMessage.contains("Mail")) {
                mailField.clear();
                mailField.requestFocus();
            } else if (validationMessage.contains("username")) {
                userField.clear();
                userField.requestFocus();
            }
        }
    }
}