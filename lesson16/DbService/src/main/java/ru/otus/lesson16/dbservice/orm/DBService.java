package ru.otus.lesson16.dbservice.orm;

import ru.otus.lesson16.dbservice.datasets.PhoneDataSet;
import ru.otus.lesson16.dbservice.datasets.UserDataSet;

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

    //void printInfo();

    void shutdown();

    //CacheEngine<? , ?> getCacheEngine();
}
