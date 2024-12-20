package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.LoginErrorHandler;
import com.projet.da50.projet_da50.controller.UserController;
import com.projet.da50.projet_da50.view.components.CustomButton;
import com.projet.da50.projet_da50.view.components.CustomLabel;
import com.projet.da50.projet_da50.view.components.CustomPasswordField;
import com.projet.da50.projet_da50.view.components.CustomTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;

public class AuthenticationFormView extends UI {

    private UserController userController;
    private Stage primaryStage;
    private LoginErrorHandler loginErrorHandler;
    private Label errorLabel;
    private GridPane grid;

    public AuthenticationFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userController = new UserController();
        this.loginErrorHandler = new LoginErrorHandler();
    }

    public void show() {
        primaryStage.setTitle("Authentication");

        // Création du GridPane principal
        this.grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Configuration pour centrer le logo sur deux colonnes
        grid.getColumnConstraints().addAll(
                new ColumnConstraints(),
                new ColumnConstraints()
        );
        grid.getColumnConstraints().get(0).setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().get(1).setHgrow(Priority.ALWAYS);

        // Création d'un rectangle pour le dégradé de fond
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        Stop[] stops = new Stop[] {
                new Stop(0, Color.web("#243447")), // Couleur de début
                new Stop(1, Color.web("#0d1117"))  // Couleur de fin
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(gradient);

        // Application de l'effet de flou gaussien à l'arrière-plan
        GaussianBlur blur = new GaussianBlur(20); // Augmenter la valeur pour plus de flou
        background.setEffect(blur);

        // Création d'un StackPane pour superposer le fond et le contenu
        StackPane root = new StackPane();
        root.getChildren().addAll(background, grid);

        // Conteneur pour les éléments du formulaire
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        // Ajust width of form container
        formContainer.setMaxWidth(WINDOW_WIDTH * 0.4);
        formContainer.setMaxHeight(WINDOW_WIDTH * 0.4);

        // Logo
        ImageView logoView = new ImageView(new Image(getClass().getResource("/icon.png").toExternalForm()));
        logoView.setFitHeight(100);
        logoView.setPreserveRatio(true);
        formContainer.getChildren().add(logoView);

        // Username
        CustomLabel userLabel = new CustomLabel("Username:");
        CustomTextField userField = new CustomTextField();
        HBox userBox = new HBox(10, userLabel, userField);
        userBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(userBox);

        // Password
        CustomLabel pwLabel = new CustomLabel("Password:");
        CustomPasswordField pwField = new CustomPasswordField();
        HBox passwordBox = new HBox(10, pwLabel, pwField);
        passwordBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(passwordBox);

        // Forgot Password Hyperlink
        Hyperlink forgotPasswordLink = new Hyperlink("forgotten password ?");
        forgotPasswordLink.getStyleClass().add("hyperlink");
        forgotPasswordLink.setOnAction(e -> {
            new ForgotPassWordFormView(primaryStage).show();
        });
        formContainer.getChildren().add(forgotPasswordLink);

        // Error Label (invisible par défaut)
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
        CustomButton btnCreateAccount = new CustomButton("Create account");
        btnCreateAccount.setOnAction(e -> {
            // Appliquer un effet de flou
            new CreateAccountFormView(primaryStage).show();
        });

        // Bottom Buttons
        HBox hbBottom = new HBox(10, btnCreateAccount, btnLogin);
        hbBottom.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(hbBottom);

        // Ajout du conteneur de formulaire au StackPane
        root.getChildren().add(formContainer);

        // Création de la scène avec le StackPane comme racine
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void handleLogin(CustomTextField userField, CustomPasswordField pwField) {
        String username = userField.getText();
        String password = pwField.getText();

        String validationMessage = loginErrorHandler.validateAuthenticationFields(username, password);
        if ("Valid credentials.".equals(validationMessage)) {
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