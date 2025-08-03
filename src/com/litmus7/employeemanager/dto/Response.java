package com.litmus7.employeemanager.dto;

public class Response<T> {
    private int statusCode;
    private String errorMessage;
    private T data;

    public Response() {}

    public Response(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
    
    public Response(int statusCode, String errorMessage, T data) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}