package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controller.LoginController;

public class LoginView {
    private final Stage stage;
    private final LoginController controller;
    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();
    private final Label messageLabel = new Label();

    public LoginView(Stage stage, LoginController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        Label bank = new Label("BAC BANK");
        bank.setStyle("-fx-font-size: 14px; -fx-text-fill: #1e88e5; -fx-font-weight: bold;");

        Label title = new Label("Welcome Back");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #0d2b52;");

        Label subtitle = new Label("Username");
        subtitle.setStyle("-fx-text-fill: #78909c;");

        String fieldStyle = "-fx-background-radius: 8; -fx-border-radius: 8; "
                + "-fx-border-color: #b0bec5; -fx-padding: 10; -fx-font-size: 14px;";

        usernameField.setPromptText("Username");
        usernameField.setStyle(fieldStyle);
        passwordField.setPromptText("Password");
        passwordField.setStyle(fieldStyle);

        Button loginButton = new Button("LOGIN");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setDefaultButton(true);
        String normal = "-fx-background-color: linear-gradient(to right, #1e88e5, #0d47a1); "
                + "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
                + "-fx-background-radius: 8; -fx-padding: 10 0 10 0; -fx-cursor: hand;";
        String hover = "-fx-background-color: linear-gradient(to right, #42a5f5, #1565c0); "
                + "-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; "
                + "-fx-background-radius: 8; -fx-padding: 10 0 10 0; -fx-cursor: hand;";
        loginButton.setStyle(normal);
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(hover));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(normal));
        loginButton.setOnAction(e -> handleLogin());

        messageLabel.setWrapText(true);

        VBox card = new VBox(12, bank, title, subtitle, usernameField, passwordField,
                loginButton, messageLabel);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(35));
        card.setMaxWidth(340);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; "
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 6);");

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0d47a1, #1e88e5, #26c6da);");

        stage.setTitle("BAC Bank - Login");
        stage.setScene(new Scene(root, 460, 520));
        stage.show();
    }

    // no business logic here: just pass the input to the controller
    private void handleLogin() {
        String error = controller.login(usernameField.getText(), passwordField.getText());
        if (error != null) {
            showMessage(error, false);
        } else {
            new DashboardView(stage, controller.createAccountController()).show();
        }
    }
    // Display login messages
    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);

        if (success) {
            messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
        } else {
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        }
    }
}