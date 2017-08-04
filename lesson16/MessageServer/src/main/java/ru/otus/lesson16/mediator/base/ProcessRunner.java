package ru.otus.lesson16.mediator.base;

import java.io.IOException;

/**
 * Created by tully.
 */
public interface ProcessRunner {
    void start(String command) throws IOException;

    void stop();

    String getOutput();
}
