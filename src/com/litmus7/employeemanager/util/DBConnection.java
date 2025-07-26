package com.litmus7.employeemanager.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String PROPERTIES_FILE = "db.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        try (FileInputStream input = new FileInputStream(PROPERTIES_FILE)) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }

        String url = props.getProperty("db.url");
        String username = props.getProperty("db.username");
        String password = props.getProperty("db.password");

        return DriverManager.getConnection(url, username, password);
    }
}