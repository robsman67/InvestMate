package com.projet.da50.projet_da50.view.authentification;

import com.projet.da50.projet_da50.controller.authentification.ErrorHandler;
import com.projet.da50.projet_da50.view.UI;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import com.projet.da50.projet_da50.view.components.CustomTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

/**
 * This class represents the view for the "Forgot Password" form.
 * It extends the UI class and provides the user interface for resetting the password.
 */
public class ForgotPassWordFormView extends UI {

    private final Stage primaryStage;
    private final ErrorHandler errorHandler;
    private Label errorLabel;

    /**
     * Constructor for ForgotPassWordFormView.
     *
     * @param primaryStage The primary stage for this view.
     */
    public ForgotPassWordFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.errorHandler = new ErrorHandler();
    }

    /**
     * Displays the "Forgot Password" form.
     */
    public void show() {
        primaryStage.setTitle("Reset Password");

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

        // Container for form elements
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        formContainer.setMaxWidth(WINDOW_WIDTH * 0.4);
        formContainer.setMaxHeight(WINDOW_HEIGHT * 0.4);

        // Label "Mail:"
        CustomLabel mailLabel = new CustomLabel("Mail:");

        // Text field for the mail
        CustomTextField mailField = new CustomTextField();

        // Add the label and text field to the form container
        formContainer.getChildren().addAll(mailLabel, mailField);

        // Error label (hidden by default)
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        formContainer.getChildren().add(errorLabel); // Add to VBox

        // "Reset Password" button
        CustomButton btnResetPassword = new CustomButton("Reset Password");
        btnResetPassword.getStyleClass().add("button-lightblue");
        btnResetPassword.setDefaultButton(true);
        btnResetPassword.setOnAction(e -> handleResetPassword(mailField, formContainer));

        // "Go back" button
        CustomButton btnBack = new CustomButton("Go back");
        btnBack.setOnAction(e -> {
            primaryStage.close();
            new AuthenticationFormView(primaryStage).show();
        });

        // Add buttons to the form container
        HBox buttonsBox = new HBox(10, btnBack, btnResetPassword);
        buttonsBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(buttonsBox);

        // Add the form container to the main GridPane
        grid.add(formContainer, 0, 0);

        // Create the scene with the StackPane as the root
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Handles the password reset process.
     *
     * @param mailField The text field for the email.
     * @param formContainer The VBox containing the form elements.
     */
    private void handleResetPassword(CustomTextField mailField, VBox formContainer) {
        String mail = mailField.getText();
        String validationMessage = errorHandler.validateForgotPasswordFields(mail);

        // Remove the old error message if it exists
        formContainer.getChildren().remove(errorLabel);

        if ("Valid credentials.".equals(validationMessage)) {
            // Display the success message
            Label successLabel = new CustomLabel("A reset link has been sent to " + mail);
            Stage successStage = new Stage();
            successStage.setTitle("Success");
            GridPane successGrid = new GridPane();
            successGrid.setAlignment(Pos.CENTER);
            successGrid.setPadding(new Insets(10));
            successGrid.add(successLabel, 0, 0);

            CustomButton closeButton = new CustomButton("Close");
            closeButton.getStyleClass().add("button-blue");
            closeButton.setOnAction(event -> {
                successStage.close();
                new AuthenticationFormView(primaryStage).show();
            });
            successGrid.add(closeButton, 0, 1);

            Scene successScene = new Scene(successGrid, 300, 100);
            successScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());
            successStage.setScene(successScene);
            successStage.showAndWait();
        } else {
            // Display the error label
            errorLabel.setText(validationMessage);
            errorLabel.setVisible(true);
            formContainer.getChildren().add(2, errorLabel); // Add as the third child

            mailField.clear();
            mailField.requestFocus();
        }
    }
}