package ru.otus.lesson16.frontend.webserver;

/**
 * Created by piphonom
 */
public interface DbAnswerProcessorsRegistry {
    void register(DbAnswerProcessor processor);
    void unregister(DbAnswerProcessor processor);
    DbAnswerProcessor getByRequestorId(long id);
}
