package ru.otus.lesson9;

import ru.otus.lesson9.connectors.MySqlSimpleConnector;
import ru.otus.lesson9.dao.DbService;
import ru.otus.lesson9.datasets.User;

/**
 * Created by piphonom
 */
public class Main {
    public static void main(String[] args) {
        try(DbService dbService = DbService.newInstance(MySqlSimpleConnector::new)) {
            User firstUser = new User("Michael Jackson", 25);
            if (dbService.save(firstUser)) {
                System.out.println("Michael Jackson is saved");
            }
            User secondUser = new User("Bob Marley", 27);
            if (dbService.save(secondUser)) {
                System.out.println("Bob Marley is saved");
            }
            User dbUser = dbService.load(1, User.class);
            System.out.println("User from DB - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

            dbUser.setAge(dbUser.getAge() + 4);
            if (dbService.update(1, dbUser)) {
                System.out.println(dbUser.getName() + " is updated");
            }

            dbUser = dbService.load(1, User.class);
            System.out.println("User from DB - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

            dbUser = dbService.load(2, User.class);
            System.out.println("User from DB - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
