package com.backend.app.exception;

public class InvalidSortingOrderException extends RuntimeException {
    public InvalidSortingOrderException(String message) {
        super(message);
    }
}
