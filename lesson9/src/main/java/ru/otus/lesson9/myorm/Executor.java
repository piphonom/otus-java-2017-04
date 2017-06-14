package ru.otus.lesson9.myorm;

import ru.otus.lesson9.base.datasets.DataSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;

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

    public <T extends DataSet> List<T> executeSelect(String query, Function<ResultSet, List<T>> dataSetConstructor) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            List<T> result = dataSetConstructor.apply(resultSet);
            resultSet.close();
            statement.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
