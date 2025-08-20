package com.litmus7.employeemanager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
public class AppErrorMessages {
    private static final Properties props = new Properties();
    
    static {
        try (InputStream input = AppErrorMessages.class.getClassLoader()
            .getResourceAsStream("ErrorMessages.properties")) {
            if (input == null) {
                throw new RuntimeException("Required properties file 'ErrorMessage.properties' not found.");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from file.", e);
        }
    }
    
    public static String getErrorMessage(String errorCode) {
        return props.getProperty(errorCode, "Unknown error code: " + errorCode);
    }
}

