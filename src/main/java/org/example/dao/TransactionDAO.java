package org.example.dao;

import org.example.model.Account;
import org.example.model.Transaction;

import java.sql.*;

public class TransactionDAO {

    // CREATE
    public void insert(String accountNumber, Transaction t) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, txn_timestamp) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ps.setString(2, t.getTransactionType());
            ps.setDouble(3, t.getAmount());
            ps.setTimestamp(4, Timestamp.valueOf(t.getTimestamp()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // READ
    public void loadInto(Account a) {
        String sql = "SELECT transaction_id, transaction_type, amount, txn_timestamp FROM transactions "
                + "WHERE account_number = ? ORDER BY transaction_id";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getAccountNumber());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    a.restoreTransaction(new Transaction(rs.getInt(1), rs.getString(2),
                            rs.getDouble(3), rs.getTimestamp(4).toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // UPDATE: rewrite the account's history so it matches memory
    public void replaceAll(Account a) {
        delete(a.getAccountNumber());
        for (Transaction t : a.getTransactions()) {
            insert(a.getAccountNumber(), t);
        }
    }

    // DELETE
    public void delete(String accountNumber) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM transactions WHERE account_number = ?")) {
            ps.setString(1, accountNumber);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }
}