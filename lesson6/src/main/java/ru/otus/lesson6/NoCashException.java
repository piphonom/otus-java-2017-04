package ru.otus.lesson6;

/**
 * Created by piphonom
 */
public class NoCashException extends Exception {
    public NoCashException() {
        super();
    }

    public NoCashException(String message) {
        super(message);
    }
}
