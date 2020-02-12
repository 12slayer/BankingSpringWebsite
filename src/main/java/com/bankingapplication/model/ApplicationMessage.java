package com.bankingapplication.model;

public class ApplicationMessage {
    private boolean success;
    private String message;
    private User body;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getBody() {
        return body;
    }

    public void setBody(User body) {
        this.body = body;
    }
}
