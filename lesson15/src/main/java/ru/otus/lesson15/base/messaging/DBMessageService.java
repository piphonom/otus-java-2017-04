package ru.otus.lesson15.base.messaging;

import ru.otus.lesson15.base.datasets.UserDataSet;

/**
 * Created by piphonom
 */
public interface DBMessageService {
    void init();

    UserDataSet getUserByName(String name);
}
