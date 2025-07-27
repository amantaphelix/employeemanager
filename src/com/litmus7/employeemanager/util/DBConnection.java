package com.litmus7.employeemanager.util;

import com.litmus7.employeemanager.property.DatabaseProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {
        String url = DatabaseProperties.getDbUrl();
        String username = DatabaseProperties.getDbUser();
        String password = DatabaseProperties.getDbPassword();

        return DriverManager.getConnection(url, username, password);
    }
}
