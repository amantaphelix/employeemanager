package com.litmus7.employeemanager;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final Dotenv dotenv = Dotenv.configure()
                                               .directory("src")
                                               .load();

    public static Connection getConnection() throws SQLException {
        final String url = dotenv.get("DB_URL");
        final String user = dotenv.get("DB_USER");
        final String password = dotenv.get("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.err.println("Error while closing connection: " + e.getMessage());
        }
    }
}
