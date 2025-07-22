package com.litmus7.employeemanager;

import java.util.List;

public class Response {
    private int statusCode;
    private String message;
    private int employeeCount;
    private List<String> errors;

    public Response(int statusCode, String message, int employeeCount, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.employeeCount = employeeCount;
        this.errors = errors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
}
