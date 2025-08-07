package com.litmus7.employeemanager.exception;

public class EmployeeServiceException extends Exception {
    public EmployeeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

	public EmployeeServiceException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
}
