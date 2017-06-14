package ru.otus.lesson9;

import ru.otus.lesson9.base.DBService;
import ru.otus.lesson9.base.datasets.UserDataSet;
import ru.otus.lesson9.implementations.myorm.DBServiceImpl;
import ru.otus.lesson9.myorm.connectors.MySqlSimpleConnector;

import java.util.List;

/**
 * Created by piphonom
 */
public class Main {
    public static void main(String[] args) {
        DBService dbService = new DBServiceImpl(MySqlSimpleConnector::new);
        UserDataSet firstUser = new UserDataSet("Michael Jackson", 25);
        if (dbService.save(firstUser)) {
            System.out.println("Michael Jackson is saved");
        }
        UserDataSet secondUser = new UserDataSet("Bob Marley", 27);
        if (dbService.save(secondUser)) {
            System.out.println("Bob Marley is saved");
        }
        UserDataSet dbUser = dbService.read(1);
        System.out.println("UserDataSet from DB - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        dbUser = dbService.readByName("Bob Marley");
        System.out.println("UserDataSet from DB - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        List<UserDataSet> users = dbService.readAll();
        System.out.println("All DB contents:");
        users.forEach(user -> System.out.println("UserDataSet from DB - id: " + user.getId() + ", name: " + user.getName() + ", age: " + user.getAge()));
    }
}
