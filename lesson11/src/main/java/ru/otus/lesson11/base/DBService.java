package ru.otus.lesson11.base;

import ru.otus.lesson11.base.datasets.PhoneDataSet;
import ru.otus.lesson11.base.datasets.UserDataSet;

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
}
