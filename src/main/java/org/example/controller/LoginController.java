package org.example.controller;

import org.example.dao.CustomerDAO;
import org.example.model.*;

public class LoginController {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private AuthenticationService auth = new AuthenticationService();

    // returns null when login works, or an error message when it fails
    public String login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return "Please enter your username and password";
        }
        try {
            Customer customer = customerDAO.findByUsername(username.trim());
            if (customer == null) return "Invalid username or password";
            auth = new AuthenticationService();
            auth.register(customer);
            return auth.login(username.trim(), password) ? null : "Invalid username or password";
        } catch (IllegalStateException e) {
            return "Database error, please try again";
        }
    }

    public String getLoggedInName() {
        return auth.getLoggedIn().getDisplayName();
    }

    public AccountController createAccountController() {
        return new AccountController(auth.getLoggedIn());
    }
}