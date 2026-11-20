package org.example;

import org.example.controller.AccountController;
import org.example.controller.LoginController;
import org.example.dao.*;
import org.example.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @BeforeAll
    static void setUp() {
        DatabaseConnection.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");   // separate test database
        DatabaseConnection.initSchema();
        DatabaseSeeder.seedIfEmpty();
    }

    private AccountController loginAsBogolo() {
        LoginController login = new LoginController();
        assertNull(login.login("bogolo", "pw7577"));
        return login.createAccountController();
    }

    private double dbBalance(String accountNumber) {
        Customer c = new CustomerDAO().findByUsername("bogolo");
        for (Account a : c.getAccounts()) {
            if (a.getAccountNumber().equals(accountNumber)) return a.getBalance();
        }
        fail("Account not found");
        return 0;
    }

    @Test
    void tenSampleCustomersExist() {
        assertEquals(10, new CustomerDAO().count());
    }

    @Test
    void loginRejectsWrongPassword() {
        assertEquals("Invalid username or password", new LoginController().login("bogolo", "wrong"));
    }

    @Test
    void depositUpdatesTheDatabase() {
        double before = dbBalance("S1001");
        assertEquals("Deposit successful", loginAsBogolo().deposit("S1001 - Savings", "250"));
        assertEquals(before + 250, dbBalance("S1001"), 0.01);
    }

    @Test
    void savingsWithdrawalIsRejected() {
        double before = dbBalance("S1001");
        assertEquals("Savings accounts do not allow withdrawals",
                loginAsBogolo().withdraw("S1001 - Savings", "100"));
        assertEquals(before, dbBalance("S1001"), 0.01);
    }

    @Test
    void chequeWithdrawalWorks() {
        double before = dbBalance("Q1001");
        assertEquals("Withdrawal successful", loginAsBogolo().withdraw("Q1001 - Cheque", "100"));
        assertEquals(before - 100, dbBalance("Q1001"), 0.01);
    }

    @Test
    void investmentNeedsBWP500() {
        String result = loginAsBogolo().openAccount("Investment", "100", "", "");
        assertTrue(result.contains("BWP500"));
    }

    @Test
    void chequeNeedsEmployer() {
        String result = loginAsBogolo().openAccount("Cheque", "", "", "");
        assertTrue(result.contains("employer"));
    }

    @Test
    void interestIsPaidAtCorrectRates() {
        double savingsBefore = dbBalance("S1001");
        double investmentBefore = dbBalance("I1001");
        loginAsBogolo().payMonthlyInterest();
        assertEquals(savingsBefore * 1.0005, dbBalance("S1001"), 0.01);
        assertEquals(investmentBefore * 1.05, dbBalance("I1001"), 0.01);
    }
}