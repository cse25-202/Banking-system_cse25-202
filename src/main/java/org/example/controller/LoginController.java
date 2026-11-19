package org.example.controller;

import org.example.model.*;

public class LoginController {
    private final AuthenticationService auth = new AuthenticationService();

    public LoginController() {
        // temporary sample customer until the database is added
        Individual demo = new Individual("C001", "Bogolo", "Samoka", "Gaborone",
                "75775398", "samokab@mail.com", "bogolo", "pw7577");
        new SavingsAccount("S001", "Rail Park Mall", demo, 1000);
        new InvestmentAccount("I001", "Rail Park Mall", demo, 1000);
        new ChequeAccount("Q001", "Rail Park Mall", demo, "BAC", "Gaborone");
        auth.register(demo);
    }

    // returns null when login works, or an error message when it fails
    public String login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "Please enter your username and password";
        }
        return auth.login(username, password) ? null : "Invalid username or password";
    }

    public String getLoggedInName() {
        return auth.getLoggedIn().getDisplayName();
    }

    public AccountController createAccountController() {
        return new AccountController(auth.getLoggedIn());
    }
}