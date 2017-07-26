package ru.otus.lesson15.base.orm;

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
