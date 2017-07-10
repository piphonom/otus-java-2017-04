package ru.otus.lesson13.implementations;

import ru.otus.lesson13.base.datasets.UserDataSet;
import ru.otus.lesson13.myorm.ORMService;
import ru.otus.lesson13.myorm.connectors.Connector;

import java.io.Closeable;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by piphonom
 */
public class UserDataSetDAO implements Closeable {

    private ORMService ormService;

    public UserDataSetDAO(Supplier<Connector> connectorFactory) {
        this.ormService = ORMService.newInstance(connectorFactory);
    }

    boolean save(UserDataSet dataSet) {
        return ormService.save(dataSet);
    }

    UserDataSet read(int id) {
        return ormService.read(id, UserDataSet.class);
    }

    UserDataSet readByName(String name) {
        return ormService.read(name, UserDataSet.class);
    }

    List<UserDataSet> readAll() {
        return ormService.readAll(UserDataSet.class);
    }

    @Override
    public void close() {
        try {
            ormService.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
