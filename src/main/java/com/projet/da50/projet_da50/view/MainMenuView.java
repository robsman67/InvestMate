package com.projet.da50.projet_da50.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainMenuView extends UI {
    private Stage primaryStage;

    public MainMenuView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void show() {

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //TODO

        Scene scene = new Scene(grid, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
