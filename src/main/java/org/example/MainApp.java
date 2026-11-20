package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.dao.DatabaseConnection;
import org.example.dao.DatabaseSeeder;
import org.example.view.LoginView;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        DatabaseConnection.initSchema();   // create tables
        DatabaseSeeder.seedIfEmpty();      // insert the 10 sample customers once
        new LoginView(stage, new LoginController()).show();
    }
}