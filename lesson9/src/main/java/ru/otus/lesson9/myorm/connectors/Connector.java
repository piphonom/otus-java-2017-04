package ru.otus.lesson9.myorm.connectors;

import java.sql.Connection;

/**
 * Created by piphonom
 */
public interface Connector extends AutoCloseable {
    Connection getConnection();

    @Override
    default void close() throws Exception {

    }
}
