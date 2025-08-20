package com.litmus7.employeemanager.exception;

public class EmployeeDaoException extends Exception {
    private final String errorCode;

    public EmployeeDaoException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public EmployeeDaoException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}