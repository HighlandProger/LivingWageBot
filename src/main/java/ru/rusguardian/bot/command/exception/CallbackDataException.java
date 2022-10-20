package ru.rusguardian.bot.command.exception;

public class CallbackDataException extends RuntimeException {

    public CallbackDataException() {
    }

    public CallbackDataException(String message) {
        super(message);
    }
}
