package org.example.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static String url = "jdbc:h2:./data/bankdb";

    public static void setUrl(String newUrl) { url = newUrl; }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, "sa", "");
    }

    // creates the tables from schema.sql if they do not exist yet
    public static void initSchema() {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/schema.sql");
             Connection c = getConnection();
             Statement st = c.createStatement()) {
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            for (String part : sql.split(";")) {
                if (!part.isBlank()) st.execute(part);
            }
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("Could not create schema: " + e.getMessage(), e);
        }
    }
}