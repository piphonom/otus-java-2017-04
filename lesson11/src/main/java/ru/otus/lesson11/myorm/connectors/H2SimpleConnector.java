package ru.otus.lesson11.myorm.connectors;

import org.h2.tools.DeleteDbFiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by piphonom
 */
public class H2SimpleConnector implements Connector {

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_DIR = ".";
    private static final String DB_NAME = "test";
    private static final String DB_CONNECTION = "jdbc:h2:" + DB_DIR + "/" + DB_NAME;
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            String createTableQuery = "CREATE TABLE IF NOT EXISTS User (" +
                    "  id int(20) PRIMARY KEY AUTO_INCREMENT," +
                    "  name varchar(255)," +
                    "  age int(3)" +
                    ")";
            PreparedStatement statement = connection.prepareStatement(createTableQuery);
            int res = statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void close() throws Exception {
        DeleteDbFiles.execute(DB_DIR, DB_NAME, true);
    }
}
