package ru.otus.lesson15.base.orm;

import ru.otus.lesson15.base.datasets.PhoneDataSet;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.cache.CacheEngine;

import java.util.List;

/**
 * Created by piphonom
 */
public interface DBService {
    String getLocalStatus();

    boolean save(UserDataSet dataSet);

    UserDataSet read(int id);

    UserDataSet readByName(String name);

    List<UserDataSet> readAll();

    List<PhoneDataSet> readPhonesByName(String name);

    void printInfo();

    void shutdown();

    CacheEngine<?, ?> getCacheEngine();
}
