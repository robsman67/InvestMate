package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.authentification.TokenManager;
import com.projet.da50.projet_da50.view.authentification.AuthenticationFormView;
import com.projet.da50.projet_da50.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.projet.da50.projet_da50.controller.authentification.TokenManager.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Verify if a token is stored
        String storedToken = TokenManager.getToken();

        if (TokenManager.isTokenValid(storedToken)) {
            stayLogged = true;
            // A valid token exists, show the main menu
            new MainMenuView(primaryStage).show();
        } else {
            // No valid token exists, show the authentication form
            new AuthenticationFormView(primaryStage).show();
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
        shutDownDeleteToken(getIdToken());
        launch(args);
    }
}