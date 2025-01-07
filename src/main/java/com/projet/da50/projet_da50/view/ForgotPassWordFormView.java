package com.projet.da50.projet_da50.view;

import com.projet.da50.projet_da50.controller.ErrorHandler;
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

public class ForgotPassWordFormView extends UI {

    private Stage primaryStage;
    private ErrorHandler errorHandler;
    private Label errorLabel;
    private GridPane grid;

    public ForgotPassWordFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.errorHandler = new ErrorHandler();
    }

    public void show() {
        primaryStage.setTitle("Reset Password");

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
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(30));
        formContainer.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 100% 100%, #5199ff, #243447); -fx-background-radius: 10;");
        formContainer.setMaxWidth(WINDOW_WIDTH * 0.4);
        formContainer.setMaxHeight(WINDOW_HEIGHT * 0.4);

        // Label "Mail:"
        CustomLabel mailLabel = new CustomLabel("Mail:");

        // Champ de texte pour le mail
        CustomTextField mailField = new CustomTextField();

        // Ajout du label et du champ de texte au conteneur du formulaire
        formContainer.getChildren().addAll(mailLabel, mailField);

        // Label d'erreur (invisible par défaut)
        errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);
        formContainer.getChildren().add(errorLabel); // Ajout au VBox

        // Bouton "Reset Password"
        CustomButton btnResetPassword = new CustomButton("Reset Password");
        btnResetPassword.getStyleClass().add("button-lightblue");
        btnResetPassword.setDefaultButton(true);
        btnResetPassword.setOnAction(e -> handleResetPassword(mailField, formContainer));

        // Back Button
        CustomButton btnBack = new CustomButton("Go back");
        btnBack.setOnAction(e -> {
            new AuthenticationFormView(primaryStage).show();
        });

        // Ajout des boutons au conteneur du formulaire
        HBox buttonsBox = new HBox(10, btnBack, btnResetPassword);
        buttonsBox.setAlignment(Pos.CENTER);
        formContainer.getChildren().add(buttonsBox);

        // Ajout du conteneur de formulaire au GridPane principal
        grid.add(formContainer, 0, 0);

        // Création de la scène avec le StackPane comme racine
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void handleResetPassword(CustomTextField mailField, VBox formContainer) {
        String mail = mailField.getText();
        String validationMessage = errorHandler.validateForgotPasswordFields(mail);

        // Supprimer l'ancien message d'erreur s'il existe
        formContainer.getChildren().remove(errorLabel);

        if ("Valid credentials.".equals(validationMessage)) {
            // Afficher le message de succès
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
            successScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            successStage.setScene(successScene);
            successStage.showAndWait();
        } else {
            // Afficher le label d'erreur
            errorLabel.setText(validationMessage);
            errorLabel.setVisible(true);
            formContainer.getChildren().add(2, errorLabel); // Ajout en tant que troisième enfant

            mailField.clear();
            mailField.requestFocus();
        }
    }
}