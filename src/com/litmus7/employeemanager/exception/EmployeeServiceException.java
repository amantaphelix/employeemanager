package com.litmus7.employeemanager.exception;

public class EmployeeServiceException extends Exception {
    private final String errorCode;

    public EmployeeServiceException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }

    public EmployeeServiceException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}