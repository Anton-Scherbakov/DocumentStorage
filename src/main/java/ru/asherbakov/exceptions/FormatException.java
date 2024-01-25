package ru.asherbakov.exceptions;

public class FormatException extends RuntimeException {
    public FormatException() {
    }

    public FormatException(String message) {
        super(message);
    }
}
