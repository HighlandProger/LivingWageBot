package ru.rusguardian.bot.command.exception;

public class NoSuchItemInfoException extends RuntimeException {

    public NoSuchItemInfoException() {
    }

    public NoSuchItemInfoException(String message) {
        super(message);
    }
}
