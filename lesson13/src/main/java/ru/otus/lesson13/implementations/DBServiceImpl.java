package ru.otus.lesson13.implementations;

import ru.otus.lesson13.base.DBService;
import ru.otus.lesson13.base.datasets.PhoneDataSet;
import ru.otus.lesson13.base.datasets.UserDataSet;
import ru.otus.lesson13.cache.CacheEngine;
import ru.otus.lesson13.cache.CacheEngineImpl;
import ru.otus.lesson13.myorm.connectors.Connector;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by piphonom
 */
public class DBServiceImpl implements DBService {

    private final int CACHE_SIZE = 10;
    private final int CACHE_IDLE_MS = 5_000;
    private final int CACHE_LIFETIME_MS = 5_000;

    private final UserDataSetDAO dao;
    CacheEngine<Integer, UserDataSet> idsCache;
    CacheEngine<String, UserDataSet> namesCache;

    public DBServiceImpl(Supplier<Connector> connectorFactory, CacheEngine<String, UserDataSet> namesCache) {
        dao = new UserDataSetDAO(connectorFactory);
        idsCache = new CacheEngineImpl<>(CACHE_SIZE, false, CACHE_IDLE_MS, CACHE_LIFETIME_MS);
        //namesCache = new CacheEngineImpl<>(CACHE_SIZE, false, CACHE_IDLE_MS, CACHE_LIFETIME_MS);
        this.namesCache = namesCache;
    }

    @Override
    public String getLocalStatus() {
        return "ACTIVE";
    }

    @Override
    public boolean save(UserDataSet dataSet) {
        dao.save(dataSet);
        namesCache.put(dataSet.getName(), dataSet);
        return true;
    }

    @Override
    public UserDataSet read(int id) {
        UserDataSet dataSet = idsCache.get(id);
        if (dataSet == null) {
            System.out.println("IDs cache is clear. Read from DAO");
            dataSet = dao.read(id);
            idsCache.put(dataSet.getId(), dataSet);
            namesCache.put(dataSet.getName(), dataSet);
        }
        return dataSet;
    }

    @Override
    public UserDataSet readByName(String name) {
        UserDataSet dataSet = namesCache.get(name);
        if (dataSet == null) {
            System.out.println("Names cache is clear. Read from DAO");
            dataSet = dao.readByName(name);
            idsCache.put(dataSet.getId(), dataSet);
            namesCache.put(dataSet.getName(), dataSet);
        }
        return dataSet;
    }

    @Override
    public List<UserDataSet> readAll() {
        return dao.readAll();
    }

    @Override
    public List<PhoneDataSet> readPhonesByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shutdown() {
        dao.close();
        idsCache.dispose();
        namesCache.dispose();
    }

    @Override
    public void printInfo() {
        System.out.println("DBService info:");
        System.out.println("Cache info:");
        namesCache.printInfo();
        System.out.println();
    }
}
