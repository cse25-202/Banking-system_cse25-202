package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.LoginController;
import org.example.view.LoginView;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        new LoginView(stage, new LoginController()).show();
    }
}