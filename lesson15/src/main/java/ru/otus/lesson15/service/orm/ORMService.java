package ru.otus.lesson15.service.orm;

import ru.otus.lesson15.base.datasets.DataSet;
import ru.otus.lesson15.base.orm.Connector;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created piphonom
 */
public class ORMService implements AutoCloseable {

    Connector connector;

    private ORMService(Connector connector) {
        this.connector = connector;
    }

    public static ORMService newInstance(Supplier<Connector> connectorFactory){
        return new ORMService(connectorFactory.get());
    }

    public <T extends DataSet> boolean save (T dataSet) {
        String updateRequest = SQLConstructor.constructSimpleInsert(dataSet);
        Executor executor = new Executor(connector.getConnection());
        return (executor.executeUpdate(updateRequest) > 0);
    }

    public <T extends DataSet> boolean update (int id, T dataSet) {
        String updateRequest = SQLConstructor.constructUpdateById(id, dataSet);

        Executor executor = new Executor(connector.getConnection());
        return (executor.executeUpdate(updateRequest) > 0);
    }

    public <T extends DataSet> T read(int id, Class<T> clazz) {
        String selectRequest = SQLConstructor.constructSelectById(id, clazz);
        List<T> result = executeSelect(selectRequest, clazz);
        return result.size() != 0 ? result.get(0) : null;
    }

    public <T extends DataSet> T read(String name, Class<T> clazz) {
        String selectRequest = SQLConstructor.constructSelectByName(name, clazz);
        List<T> result = executeSelect(selectRequest, clazz);
        return result.size() != 0 ? result.get(0) : null;
    }

    public <T extends DataSet> List<T> readAll(Class<T> clazz) {
        String selectRequest = SQLConstructor.constructSelectAll(clazz);
        return executeSelect(selectRequest, clazz);
    }

    @Override
    public void close() throws Exception {
        if (connector != null) {
            connector.close();
        }
    }

    private <T extends DataSet> List<T> executeSelect(String request, Class<T> clazz) {
        Executor executor = new Executor(connector.getConnection());
        return executor.executeSelect(request, rs -> {
            List<T> list = new ArrayList<T>();
            try {
                while (rs.next()) {
                    list.add(buildDataSet(rs, clazz));
                }
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private static <T extends DataSet> T buildDataSet(ResultSet resultSet, Class<T> clazz) {
        try {
            T dataSet = clazz.newInstance();
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                try {
                    Column columnAnno = field.getAnnotation(Column.class);
                    if (columnAnno == null)
                        return;
                    String columnName = columnAnno.name().equals("") ? field.getName() : columnAnno.name();

                    field.setAccessible(true);
                    switch(field.getType().getSimpleName()) {
                        case ("Integer"):
                        case ("int"):
                            field.set(dataSet, resultSet.getInt(columnName));
                            break;
                        case ("String"):
                            field.set(dataSet, resultSet.getString(columnName));
                    }
                    field.setAccessible(false);
                } catch (IllegalAccessException | SQLException e) {
                    e.printStackTrace();
                }
            });
            try {
                Field idField = clazz.getSuperclass().getDeclaredField("id");
                Column columnAnno = idField.getAnnotation(Column.class);
                if (columnAnno != null) {
                    String columnName = columnAnno.name().equals("") ? idField.getName() : columnAnno.name();
                    idField.setAccessible(true);
                    idField.set(dataSet, resultSet.getInt(columnName));
                }
            } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }

            return dataSet;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
