package com.example.minesweeper;

public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(String errorMessage) {
        super(errorMessage);
    }
}
