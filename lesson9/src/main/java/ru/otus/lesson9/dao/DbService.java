package ru.otus.lesson9.dao;

import ru.otus.lesson9.helpers.SQLConstructor;
import ru.otus.lesson9.connectors.Connector;
import ru.otus.lesson9.datasets.DataSet;

import java.util.function.Supplier;

/**
 * Created piphonom
 */
public class DbService implements AutoCloseable {

    Connector connector;

    private DbService(Connector connector) {
        this.connector = connector;
    }

    public static DbService newInstance(Supplier<Connector> connectorFactory){
        return new DbService(connectorFactory.get());
    }

    public <T extends DataSet> boolean save (T dataSet) {
        String updateRequest = SQLConstructor.constructSimpleInsert(dataSet);
        if (updateRequest != null) {
            Executor executor = new Executor(connector.getConnection());
            if (executor.executeUpdate(updateRequest) > 0)
                return true;
        }
        return false;
    }

    public <T extends DataSet> boolean update (int id, T dataSet) {
        String updateRequest = SQLConstructor.constructUpdateById(id, dataSet);
        if (updateRequest != null) {
            Executor executor = new Executor(connector.getConnection());
            if (executor.executeUpdate(updateRequest) > 0)
                return true;
        }
        return false;
    }

    public <T extends DataSet> T load(int id, Class<T> clazz) {
        String selectRequest = SQLConstructor.constructSelectById(id, clazz);
        if (selectRequest != null) {
            Executor executor = new Executor(connector.getConnection());
            return executor.executeQuery(selectRequest, clazz);
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (connector != null) {
            connector.close();
        }
    }
}
