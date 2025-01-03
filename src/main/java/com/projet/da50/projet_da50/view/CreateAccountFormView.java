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
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class CreateAccountFormView extends UI {

    private UserController userController;
    private Stage primaryStage;
    private LoginErrorHandler loginErrorHandler;
    private Label errorLabel;
    private GridPane grid;

    public CreateAccountFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.userController = new UserController();
        this.loginErrorHandler = new LoginErrorHandler();
    }

    @Override
    public void show() {
        primaryStage.setTitle("Create Account");

        // Création du GridPane principal
        this.grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Création d'un rectangle pour le dégradé de fond
        Rectangle background = new Rectangle(WINDOW_WIDTH, WINDOW_HEIGHT);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.web("#243447")), // Couleur de début
                new Stop(1, Color.web("#0d1117"))  // Couleur de fin
        };
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        background.setFill(gradient);

        // Application de l'effet de flou gaussien à l'arrière-plan
        GaussianBlur blur = new GaussianBlur(20);
        background.setEffect(blur);

        // Création d'un StackPane pour superposer le fond et le contenu
        StackPane root = new StackPane();
        root.getChildren().addAll(background, grid);

        // Conteneur pour les éléments du formulaire
        GridPane formGrid = new GridPane(); // Utilisation d'un GridPane pour le formulaire
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(30));
        formGrid.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        formGrid.setMaxWidth(WINDOW_WIDTH * 0.4);
        formGrid.setMaxHeight(WINDOW_HEIGHT * 0.5);

        // Username
        CustomLabel userLabel = new CustomLabel("Username:");
        CustomTextField userField = new CustomTextField();
        formGrid.add(userLabel, 0, 0);
        formGrid.add(userField, 1, 0);

        // Mail
        CustomLabel mailLabel = new CustomLabel("Mail:");
        CustomTextField mailField = new CustomTextField();
        formGrid.add(mailLabel, 0, 1);
        formGrid.add(mailField, 1, 1);

        // Password
        CustomLabel pwLabel = new CustomLabel("Password:");
        CustomPasswordField pwField = new CustomPasswordField();
        formGrid.add(pwLabel, 0, 2);
        formGrid.add(pwField, 1, 2);

        // Error Label (invisible par défaut)
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        formGrid.add(errorLabel, 0, 3, 2, 1); // Span sur deux colonnes

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

        // Ajout des boutons au conteneur du formulaire
        HBox buttonsBox = new HBox(10, btnBack, btnCreateAccount);
        buttonsBox.setAlignment(Pos.CENTER);
        formGrid.add(buttonsBox, 0, 4, 2, 1); // Span sur deux colonnes

        // Ajout du conteneur de formulaire au GridPane principal
        grid.add(formGrid, 0, 0);

        // Création de la scène avec le StackPane comme racine
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void handleCreateAccount(CustomTextField userField, CustomPasswordField pwField, CustomTextField mailField, GridPane formGrid) {
        String username = userField.getText();
        String password = pwField.getText();
        String mail = mailField.getText();

        // Supprimer l'ancien message d'erreur s'il existe
        formGrid.getChildren().remove(errorLabel);

        String validationMessage = loginErrorHandler.validateCreateAccountFields(username, password, mail);
        if ("Valid credentials.".equals(validationMessage)) {
            userController.createUser(username, password, mail);
            new AuthenticationFormView(primaryStage).show();
        } else {
            errorLabel.setText(validationMessage);
            errorLabel.setVisible(true);
            formGrid.add(errorLabel, 0, 3, 2, 1); // Span sur deux colonnes

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