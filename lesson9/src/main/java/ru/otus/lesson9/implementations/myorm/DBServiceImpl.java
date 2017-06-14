package ru.otus.lesson9.implementations.myorm;

import ru.otus.lesson9.base.DBService;
import ru.otus.lesson9.myorm.connectors.Connector;
import ru.otus.lesson9.base.datasets.UserDataSet;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by piphonom
 */
public class DBServiceImpl implements DBService {

    private final UserDataSetDAO dao;

    public DBServiceImpl(Supplier<Connector> connectorFactory) {
        dao = new UserDataSetDAO(connectorFactory);
    }

    @Override
    public String getLocalStatus() {
        return "ACTIVE";
    }

    @Override
    public boolean save(UserDataSet dataSet) {
        return dao.save(dataSet);
    }

    @Override
    public UserDataSet read(int id) {
        return dao.read(id);
    }

    @Override
    public UserDataSet readByName(String name) {
        return dao.readByName(name);
    }

    @Override
    public List<UserDataSet> readAll() {
        return dao.readAll();
    }

    @Override
    public void shutdown() {
        dao.close();
    }
}
