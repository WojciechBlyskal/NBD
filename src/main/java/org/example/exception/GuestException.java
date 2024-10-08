package org.example.exception;

public class GuestException extends RuntimeException {
    private String message;
    public GuestException(String err_message) {
        super(err_message);
        this.message = err_message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}


