package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/banco";  // Change this to your database name
    private static final String USER = "root";  // Default XAMPP MySQL username
    private static final String PASSWORD = "";  // Default XAMPP MySQL password is empty (if you set a password, change it)

    // Method that establishes the database connection
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
            throw e;
        }
    }

    // Method to test the connection to the database
    public static boolean testConnection() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Execute a simple query to check the connection
            stmt.executeQuery("SELECT 1");
            System.out.println("Database connection successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("Error testing the connection: " + e.getMessage());
            return false;
        }
    }

    // Main method for testing the connection
    public static void main(String[] args) {
        testConnection();
    }
}
