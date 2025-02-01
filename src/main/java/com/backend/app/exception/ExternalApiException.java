package com.backend.app.exception;

public class ExternalApiException extends Exception {
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
