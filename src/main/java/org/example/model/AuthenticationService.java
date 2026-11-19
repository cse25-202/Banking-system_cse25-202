package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private final List<Customer> customers = new ArrayList<>();
    private Customer loggedIn;

    public void register(Customer c) {
        customers.add(c);
    }

    public boolean login(String username, String password) {
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.checkPassword(password)) {
                loggedIn = c;
                return true;
            }
        }
        return false;
    }

    public void logOut() {
        loggedIn = null;
    }

    public Customer getLoggedIn() {
        return loggedIn;
    }
}