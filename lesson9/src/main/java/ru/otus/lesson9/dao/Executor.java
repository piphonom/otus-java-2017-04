package ru.otus.lesson9.dao;

import ru.otus.lesson9.datasets.DataSet;

import javax.persistence.Column;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

/**
 * Created by piphonom
 */

public class Executor {
    Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public int executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            int count = statement.executeUpdate(query);
            statement.close();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <T extends DataSet> T executeQuery(String query, Class<T> clazz) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            T result =buildDataSet(resultSet, clazz);
            resultSet.close();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private <T extends DataSet> T buildDataSet(ResultSet resultSet, Class<T> clazz) {
        try {
            T dataSet = clazz.newInstance();
            if (!resultSet.next())
                return null;
            Arrays.stream(clazz.getDeclaredFields()).forEach(field -> {
                try {
                    Column columnAnno = field.getAnnotation(Column.class);
                    if (columnAnno == null)
                        return;
                    String columnName = columnAnno.name() != "" ? columnAnno.name() : field.getName();
                    if (columnAnno != null) {
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
                    }
                } catch (IllegalAccessException | SQLException e) {
                    e.printStackTrace();
                }
            });
            return dataSet;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
