package org.example.dao;

import org.example.model.*;

import java.sql.*;

public class AccountDAO {

    // CREATE or UPDATE (inserts the row, or updates it if it exists)
    public void save(Account a) {
        String sql = "MERGE INTO accounts (account_number, account_type, balance, branch, "
                + "employer_name, employer_address, customer_id) KEY (account_number) VALUES (?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            ps.setString(2, a.getAccountType());
            ps.setDouble(3, a.getBalance());
            ps.setString(4, a.getBranch());
            if (a instanceof ChequeAccount q) {
                ps.setString(5, q.getCompanyName());
                ps.setString(6, q.getCompanyAddress());
            } else {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, a.getOwner().getCustomerID());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
        new TransactionDAO().replaceAll(a);
    }

    // READ: loads all accounts (with history) of a customer
    public void loadInto(Customer owner) {
        String sql = "SELECT account_number, account_type, balance, branch, employer_name, employer_address "
                + "FROM accounts WHERE customer_id = ? ORDER BY account_number";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, owner.getCustomerID());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String number = rs.getString(1);
                    String type = rs.getString(2);
                    String branch = rs.getString(4);
                    Account a = switch (type) {
                        case "Savings" -> new SavingsAccount(number, branch, owner, 0);
                        case "Investment" -> new InvestmentAccount(number, branch, owner);
                        default -> new ChequeAccount(number, branch, owner, rs.getString(5), rs.getString(6));
                    };
                    a.restoreBalance(rs.getDouble(3));
                    new TransactionDAO().loadInto(a);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // UPDATE (balance only)
    public void updateBalance(String accountNumber, double balance) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE accounts SET balance = ? WHERE account_number = ?")) {
            ps.setDouble(1, balance);
            ps.setString(2, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // DELETE
    public void delete(String accountNumber) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM accounts WHERE account_number = ?")) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    public int count() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM accounts")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }
}