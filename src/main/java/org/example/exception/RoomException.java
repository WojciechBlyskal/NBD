package org.example.exception;

public class RoomException extends RuntimeException {
    private String message;
    public RoomException(String err_message) {
        super(err_message);
        this.message = err_message;
    }
    @Override
    public String getMessage() {
        return message;
    }
}
