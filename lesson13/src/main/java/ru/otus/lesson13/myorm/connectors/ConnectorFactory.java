package ru.otus.lesson13.myorm.connectors;

import java.util.function.Supplier;

/**
 * Created by piphonom
 */
public class ConnectorFactory<T> implements Supplier<T> {

    T value;

    public ConnectorFactory(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }
}
