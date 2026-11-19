package org.example.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.controller.AccountController;

public class DashboardView {
    private final Stage stage;
    private final AccountController controller;

    private final ComboBox<String> accountBox = new ComboBox<>();
    private final Label balanceLabel = new Label();
    private final TextField amountField = new TextField();
    private final ListView<String> historyList = new ListView<>();
    private final Label messageLabel = new Label();

    public DashboardView(Stage stage, AccountController controller) {
        this.stage = stage;
        this.controller = controller;
    }

    public void show() {
        Label title = new Label("Welcome, " + controller.getCustomerName());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        accountBox.setPromptText("Select account");
        accountBox.setMaxWidth(Double.MAX_VALUE);
        accountBox.setOnAction(e -> refresh());

        balanceLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #69f0ae;");

        amountField.setPromptText("Amount (BWP)");
        amountField.setStyle(fieldStyle());

        Button deposit = button("Deposit");
        deposit.setOnAction(e -> run(controller.deposit(accountBox.getValue(), amountField.getText())));
        Button withdraw = button("Withdraw");
        withdraw.setOnAction(e -> run(controller.withdraw(accountBox.getValue(), amountField.getText())));
        Button interest = button("Pay Interest");
        interest.setOnAction(e -> run(controller.payMonthlyInterest()));
        Button open = button("Open Account");
        open.setOnAction(e -> openAccountDialog());

        HBox actions = new HBox(10, deposit, withdraw, interest, open);

        Label historyTitle = new Label("Transaction History");
        historyTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        historyList.setPrefHeight(220);

        Button logout = button("Logout");
        logout.setOnAction(e -> stage.close());

        messageLabel.setWrapText(true);

        VBox root = new VBox(14, title, accountBox, balanceLabel, amountField, actions,
                historyTitle, historyList, messageLabel, logout);
        root.setPadding(new Insets(28));
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #000010, #0b0b6a, #000010);");

        reloadAccounts();
        stage.setTitle("BAC Bank - Dashboard");
        stage.setScene(new Scene(root, 620, 700));
        stage.show();
    }

    private void reloadAccounts() {
        String selected = accountBox.getValue();
        accountBox.setItems(FXCollections.observableArrayList(controller.getAccountLabels()));
        if (selected != null && accountBox.getItems().contains(selected)) {
            accountBox.setValue(selected);
        } else if (!accountBox.getItems().isEmpty()) {
            accountBox.setValue(accountBox.getItems().get(0));
        }
        refresh();
    }

    private void refresh() {
        String label = accountBox.getValue();
        balanceLabel.setText(label == null ? "" : controller.getBalanceText(label));
        historyList.setItems(FXCollections.observableArrayList(
                label == null ? java.util.List.of() : controller.getTransactionLines(label)));
    }

    private void run(String result) {
        boolean ok = result.contains("successful") || result.contains("paid") || result.contains("opened");
        messageLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: " + (ok ? "#69f0ae" : "#ff6b6b") + ";");
        messageLabel.setText(result);
        amountField.clear();
        reloadAccounts();
    }

    private void openAccountDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Open Account");
        ComboBox<String> type = new ComboBox<>(FXCollections.observableArrayList("Savings", "Investment", "Cheque"));
        type.setValue("Savings");
        TextField deposit = new TextField();
        deposit.setPromptText("Opening deposit (BWP)");
        TextField employer = new TextField();
        employer.setPromptText("Employer name (Cheque only)");
        TextField employerAddress = new TextField();
        employerAddress.setPromptText("Employer address (Cheque only)");
        employer.setDisable(true);
        employerAddress.setDisable(true);
        type.setOnAction(e -> {
            boolean cheque = "Cheque".equals(type.getValue());
            employer.setDisable(!cheque);
            employerAddress.setDisable(!cheque);
        });

        VBox box = new VBox(10, new Label("Account type"), type, deposit, employer, employerAddress);
        box.setPadding(new Insets(15));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                run(controller.openAccount(type.getValue(), deposit.getText(),
                        employer.getText(), employerAddress.getText()));
            }
        });
    }

    private Button button(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: white; -fx-text-fill: #222; -fx-font-weight: bold; "
                + "-fx-background-radius: 20; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        return b;
    }

    private String fieldStyle() {
        return "-fx-background-color: rgba(255,255,255,0.08); -fx-text-fill: white; "
                + "-fx-prompt-text-fill: rgba(255,255,255,0.7); -fx-background-radius: 20; "
                + "-fx-border-color: rgba(255,255,255,0.4); -fx-border-radius: 20; -fx-padding: 8 16 8 16;";
    }
}