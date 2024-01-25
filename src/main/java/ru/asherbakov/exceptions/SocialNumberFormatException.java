package ru.asherbakov.exceptions;

public class SocialNumberFormatException extends RuntimeException {
    public SocialNumberFormatException() {
    }

    public SocialNumberFormatException(String message) {
        super(message);
    }
}
