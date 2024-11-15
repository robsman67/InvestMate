package com.projet.da50.projet_da50.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ForgotPassWordFormView extends UI {

    private Stage primaryStage;

    public ForgotPassWordFormView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {
        primaryStage.setTitle("Forgot Password");


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label emailLabel = new Label("Email:");
        grid.add(emailLabel, 0, 0);
        TextField emailTextField = new TextField();
        grid.add(emailTextField, 1, 0);

        Button btnResetPassword = new Button("Réinitialiser le mot de passe");
        btnResetPassword.setOnAction(e -> {
            System.out.println("Lien de réinitialisation envoyé à " + emailTextField.getText());
            new AuthentificationFormView(primaryStage).show();
        });

        Button btnBack = new Button("Retour");
        btnBack.setOnAction(e -> new AuthentificationFormView(primaryStage).show());

        HBox hbButtons = new HBox(10);
        hbButtons.setAlignment(Pos.BOTTOM_RIGHT);
        hbButtons.getChildren().addAll(btnBack, btnResetPassword);
        grid.add(hbButtons, 1, 1);

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Mot de passe oublié");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}