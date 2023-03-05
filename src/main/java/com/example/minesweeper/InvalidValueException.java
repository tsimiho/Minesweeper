package com.example.minesweeper;

public class InvalidValueException extends Exception {
    public InvalidValueException(String errorMessage) {
        super(errorMessage);
    }
}
