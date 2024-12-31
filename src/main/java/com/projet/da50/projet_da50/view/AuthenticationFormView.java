package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.TokenManager;
import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.model.Role;
import com.projet.da50.projet_da50.model.User;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import com.projet.da50.projet_da50.view.components.CustomPasswordField;
import com.projet.da50.projet_da50.view.components.CustomTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static com.projet.da50.projet_da50.controller.TokenManager.logController;

/**
 * The AuthenticationFormView class represents the login view for the application.
 * It allows users to authenticate by entering their username and password.
 */
public class AuthenticationFormView extends UI {

    private final UserController userController;
    private final Stage primaryStage;
    private final ErrorHandler ErrorHandler;
    private Label errorLabel;
    private GridPane grid;
    private CheckBox stayConnectedCheckBox; // Checkbox for "Stay Connected"

    /**
     * Constructs the AuthenticationFormView.
     *
     * @param primaryStage the primary stage of the application.
     */
    public AuthenticationFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userController = new UserController();
        this.loginErrorHandler = new LoginErrorHandler();
    }

    /**
     * Displays the authentication form.
     */
    public void show() {
        primaryStage.setTitle("Authentication");
        primaryStage.setMaximized(false);

        // Create the main GridPane layout
        this.grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Configure grid columns to center the logo across two columns
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(),
                new ColumnConstraints()
        );
        grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);

        // Create a rectangle for the background gradient
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#243447")), // Start color
                new Stop(1, Color.web("#0d1117"))  // End color
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(gradient);

        // Apply Gaussian blur effect to the background
        GaussianBlur blur = new GaussianBlur(20);
        background.setEffect(blur);

        // Create a StackPane to overlay the background and content
        StackPane root = new StackPane();
        root.getChildren().addAll(background, grid);

        // Container for the form elements
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        formContainer.setMaxWidth(WINDOW_WIDTH * 0.4);
        formContainer.setMaxHeight(WINDOW_WIDTH * 0.4);

        // Logo
        ImageView logoView = new ImageView(new Image(getClass().getResource("/icon.png").toExternalForm()));
        logoView.setFitHeight(100);
        logoView.setPreserveRatio(true);
        formContainer.getChildren().add(logoView);

        // Username field
        CustomLabel userLabel = new CustomLabel("Username:");
        CustomTextField userField = new CustomTextField();
        HBox userBox = new HBox(10, userLabel, userField);
        userBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(userBox);

        // Password field
        CustomLabel pwLabel = new CustomLabel("Password:");
        CustomPasswordField pwField = new CustomPasswordField();
        HBox passwordBox = new HBox(10, pwLabel, pwField);
        passwordBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(passwordBox);

        stayConnectedCheckBox = new CheckBox("Stay connected");
        stayConnectedCheckBox.setStyle("-fx-font-size: 14; -fx-text-fill: white;");
        stayConnectedCheckBox.setAlignment(Pos.CENTER);

        // Add checkbox to form container
        formContainer.getChildren().add(stayConnectedCheckBox);


        // Forgot Password Hyperlink
        Hyperlink forgotPasswordLink = new Hyperlink("Forgotten password?");
        forgotPasswordLink.getStyleClass().add("hyperlink");
        forgotPasswordLink.setOnAction(e -> new ForgotPassWordFormView(primaryStage).show());
        formContainer.getChildren().add(forgotPasswordLink);

        // Error Label (initially invisible)
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        formContainer.getChildren().add(errorLabel);

        // Login Button
        CustomButton btnLogin = new CustomButton("Login");
        btnLogin.getStyleClass().add("button-lightblue");
        btnLogin.setOnAction(e -> handleLogin(userField, pwField));

        // Create Account Button
        CustomButton btnCreateAccount = new CustomButton("Create Account");
        btnCreateAccount.setOnAction(e -> new CreateAccountFormView(primaryStage).show());

        // Bottom Buttons
        HBox hbBottom = new HBox(10, btnCreateAccount, btnLogin);
        hbBottom.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(hbBottom);

        // Add the form container to the StackPane
        root.getChildren().add(formContainer);

        // Create the scene with the StackPane as root
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * Handles the login logic.
     *
     * @param userField the text field for the username.
     * @param pwField   the password field for the password.
     */
    private void handleLogin(CustomTextField userField, CustomPasswordField pwField) {
        String username = userField.getText();
        String password = pwField.getText();

        String validationMessage = errorHandler.validateAuthenticationFields(username, password);
        if (validationMessage.startsWith("Valid credentials.")) {
            System.out.println("Login successful.");

            User user = userController.findUserByUsername(username);

            if (user.getRole() != Role.Admin) {
                setAdmin(false);
                // Generate a token for the user
                String token = TokenManager.generateToken(username);

                // Enregistrer le token en fonction de la case à cocher
                if (stayConnectedCheckBox.isSelected()) {
                    TokenManager.saveToken(token, true); // Enregistrer le token pour 24 heures
                    logController.createLog(user.getId(), "Login", "User logged in with token.");
                    System.out.println("Token saved for 24 hours.");
                } else {
                    stayLogged = false;
                    // Pour une connexion non persistante on enregistre quand même le token en mémoire
                    logController.createLog(getIdToken(), "Login", "User logged in without token.");
                    System.out.println("Token generated for the current session only.");
                }
            } else {
                setAdmin(true);
                System.out.println("Admin login, no token generated.");
            }

            new MainMenuView(primaryStage).show();
        } else {
            // Gérer l'affichage du message d'erreur
            if (errorLabel != null) {
                errorLabel.setText(validationMessage);
                errorLabel.setVisible(true);
            }

            userField.clear();
            pwField.clear();
            userField.requestFocus();
        }
    }
}