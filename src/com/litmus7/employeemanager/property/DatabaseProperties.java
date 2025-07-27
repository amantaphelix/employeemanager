package com.litmus7.employeemanager.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class DatabaseProperties {

    private static final Properties PROPERTIES = new Properties(); 
    private static final String PROPERTIES_FILE_NAME = "db.properties";

    static {
        loadProperties();
    }

    private DatabaseProperties() {}
    
    private static void loadProperties() {
        try (InputStream input = DatabaseProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME)) {
            if (input == null) {
                System.err.println("Sorry, unable to find " + PROPERTIES_FILE_NAME + " in the classpath.");
                throw new RuntimeException("Database properties file not found: " + PROPERTIES_FILE_NAME);
                

            }
            PROPERTIES.load(input);
        } catch (IOException ex) {
            System.err.println("Error loading database properties: " + ex.getMessage());
            throw new RuntimeException("Failed to load database properties", ex);
        }
    }

    public static String getDbUrl() {
        return PROPERTIES.getProperty("db.url");
    }

    public static String getDbUser() {
        return PROPERTIES.getProperty("db.username");
    }

    public static String getDbPassword() {
        return PROPERTIES.getProperty("db.password");
    }
}