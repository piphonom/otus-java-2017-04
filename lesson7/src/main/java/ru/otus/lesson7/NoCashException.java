package ru.otus.lesson7;

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
