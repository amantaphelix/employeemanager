package com.litmus7.employeemanager.exception;

public class EmployeeDaoException extends Exception {
    public EmployeeDaoException(String message) {
        super(message);
    }

    public EmployeeDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
