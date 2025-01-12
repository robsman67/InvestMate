package com.projet.da50.projet_da50;

import com.projet.da50.projet_da50.controller.authentification.TokenManager;
import com.projet.da50.projet_da50.view.authentification.AuthenticationFormView;
import com.projet.da50.projet_da50.view.MainMenuView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.projet.da50.projet_da50.controller.authentification.TokenManager.*;

/**
 * Main class for the application that initializes and launches the JavaFX application.
 * This class manages user authentication and the primary stages of the application lifecycle.
 */
public class Main extends Application {

    /**
     * Entry point for the JavaFX application. Handles initial authentication and sets up the primary stage.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        // Retrieve the stored token
        String storedToken = TokenManager.getToken();

        // Check if the token is valid
        if (TokenManager.isTokenValid(storedToken)) {
            stayLogged = true;
            // If a valid token exists, show the main menu
            new MainMenuView(primaryStage).show();
        } else {
            // If no valid token exists, show the authentication form
            new AuthenticationFormView(primaryStage).show();
        }

        // Set the application icon
        Image icon = new Image(getClass().getResource("/icon.png").toExternalForm());
        primaryStage.getIcons().add(icon);

        // Add an event listener for when the primary stage is closed
        primaryStage.setOnCloseRequest(event -> {
            if (!primaryStage.isMaximized()) {
                // If the application is closed without being maximized, shut down Hibernate and exit
                HibernateUtil.shutdown();
                System.exit(0);
            }
        });
    }

    /**
     * The main method that serves as the entry point for the application.
     * This method cleans up any existing tokens and launches the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // Clean up by deleting the token and shutting down any related services
        shutDownDeleteToken(getIdToken());

        // Launch the JavaFX application
        launch(args);
    }
}
