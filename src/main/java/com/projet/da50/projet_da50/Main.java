package com.projet.da50.projet_da50;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;
import com.projet.da50.projet_da50.view.AuthenticationFormView;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage) {
        AuthenticationFormView authPage = new AuthenticationFormView(primaryStage);
        authPage.show();

        // Définir l'icône de l'application
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
        launch(args);
    }
}