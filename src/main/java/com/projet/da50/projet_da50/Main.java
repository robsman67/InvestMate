package com.projet.da50.projet_da50;

import javafx.stage.Stage;
import javafx.application.Application;
import com.projet.da50.projet_da50.view.AuthentificationFormView;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) {
        AuthentificationFormView authPage = new AuthentificationFormView(primaryStage);
        authPage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
