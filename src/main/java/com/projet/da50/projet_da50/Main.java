package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.TokenManager;
import com.projet.da50.projet_da50.view.AuthenticationFormView;
import com.projet.da50.projet_da50.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.projet.da50.projet_da50.controller.TokenManager.shutDownDeleteToken;
import static com.projet.da50.projet_da50.controller.TokenManager.stayLogged;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Vérifier si un token valide existe
        String storedToken = TokenManager.getToken();

        if (TokenManager.isTokenValid(storedToken)) {
            stayLogged = true;
            // Un token valide existe, on connecte l'utilisateur directement
            new MainMenuView(primaryStage).show();
        } else {
            // Aucun token valide n'existe, afficher la vue d'authentification
            AuthenticationFormView authPage = new AuthenticationFormView(primaryStage);
            authPage.show();
        }

        // Set the application icon
        Image icon = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        // Listen to the close event of the authentication window
        primaryStage.setOnCloseRequest(event -> {
            if (!primaryStage.isMaximized()) {
                // if the authentication window is closed without being in full screen,
                // this means that the user is not connected, so we close the applicationa
                HibernateUtil.shutdown();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        shutDownDeleteToken();
        launch(args);
    }
}