package org.example.dao;

import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // CREATE
    public void insert(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, customer_type, first_name, surname, company_name, "
                + "registration_name, address, phone, email, username, password) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, customer.getCustomerID());
            if (customer instanceof Individual i) {
                ps.setString(2, "INDIVIDUAL");
                ps.setString(3, i.getFirstName());
                ps.setString(4, i.getSurname());
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setString(8, i.getPhoneNumber());
                ps.setString(9, i.getEmail());
            } else if (customer instanceof Company co) {
                ps.setString(2, "COMPANY");
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);
                ps.setString(5, co.getCompanyName());
                ps.setString(6, co.getRegistrationName());
                ps.setString(8, co.getTelephoneNumber());
                ps.setString(9, co.getCompanyEmail());
            }
            ps.setString(7, customer.getAddress());
            ps.setString(10, customer.getUsername());
            ps.setString(11, customer.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // READ one (returns null if not found)
    public Customer findByUsername(String username) {
        return findOne("SELECT * FROM customers WHERE username = ?", username);
    }

    public Customer findById(String customerId) {
        return findOne("SELECT * FROM customers WHERE customer_id = ?", customerId);
    }

    // READ all
    public List<Customer> findAll() {
        List<Customer> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM customers ORDER BY customer_id")) {
            while (rs.next()) list.add(read(rs));
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
        return list;
    }

    // UPDATE
    public void updateAddress(String customerId, String newAddress) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE customers SET address = ? WHERE customer_id = ?")) {
            ps.setString(1, newAddress);
            ps.setString(2, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    // DELETE (accounts and transactions are removed by ON DELETE CASCADE)
    public void delete(String customerId) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
            ps.setString(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    public int count() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM customers")) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    private Customer findOne(String sql, String value) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? read(rs) : null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Database error: " + e.getMessage(), e);
        }
    }

    private Customer read(ResultSet rs) throws SQLException {
        Customer customer;
        if ("INDIVIDUAL".equals(rs.getString("customer_type"))) {
            customer = new Individual(rs.getString("customer_id"), rs.getString("first_name"),
                    rs.getString("surname"), rs.getString("address"), rs.getString("phone"),
                    rs.getString("email"), rs.getString("username"), rs.getString("password"));
        } else {
            customer = new Company(rs.getString("customer_id"), rs.getString("company_name"),
                    rs.getString("registration_name"), rs.getString("address"), rs.getString("email"),
                    rs.getString("phone"), rs.getString("username"), rs.getString("password"));
        }
        new AccountDAO().loadInto(customer);
        return customer;
    }
}