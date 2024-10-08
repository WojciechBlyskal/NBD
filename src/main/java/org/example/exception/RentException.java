package org.example.exception;

public class RentException extends RuntimeException {
    private String message;
    public RentException(String err_message) {
        super(err_message);
        this.message = err_message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
