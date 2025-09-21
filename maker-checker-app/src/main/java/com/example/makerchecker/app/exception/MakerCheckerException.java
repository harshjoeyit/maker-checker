package com.example.makerchecker.app.exception;

public class MakerCheckerException extends RuntimeException {
    public MakerCheckerException(String message) {
        super(message);
    }
    
    public MakerCheckerException(String message, Throwable cause) {
        super(message, cause);
    }

    public String toString() {
        return "Maker checker violation";
    }
}