package ru.otus.lesson9.myorm.connectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by piphonom
 */
public class MySqlSimpleConnector implements Connector {

    @Override
    public Connection getConnection() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mysql://localhost:3306/otus_lesson9?useSSL=false&user=piphonom&password=monohpip&serverTimezone=UTC";

            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
