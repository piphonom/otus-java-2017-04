package ru.otus.lesson9;

import ru.otus.lesson9.base.DBService;
import ru.otus.lesson9.base.datasets.PhoneDataSet;
import ru.otus.lesson9.base.datasets.UserDataSet;
import ru.otus.lesson9.implementations.hibernate.DBServiceHibernateImpl;
import ru.otus.lesson9.implementations.myorm.DBServiceImpl;
import ru.otus.lesson9.myorm.connectors.MySqlSimpleConnector;

import java.util.List;

/**
 * Created by piphonom
 */
public class Main {
    public static void main(String[] args) {
        /* My ORM */
        System.out.println("My ORM:");
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
        System.out.println("UserDataSet by id - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        dbUser = dbService.readByName("Bob Marley");
        System.out.println("UserDataSet by name - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        List<UserDataSet> users = dbService.readAll();
        System.out.println("All DB content:");
        users.forEach(user -> System.out.println("UserDataSet from DB - id: " + user.getId() + ", name: " + user.getName() + ", age: " + user.getAge()));
        dbService.shutdown();

        /* Hibernate */
        System.out.println("\nHibernate:");
        DBService dbServiceHibernate = new DBServiceHibernateImpl();

        String status = dbServiceHibernate.getLocalStatus();
        System.out.println("Status: " + status);

        String userName = "Mickey Mouse";
        UserDataSet mickey = new UserDataSet(userName, 89);
        mickey.setPhone(new PhoneDataSet("12345"));
        mickey.setPhone(new PhoneDataSet("67890"));

        dbServiceHibernate.save(mickey);
        System.out.println(userName + " is saved");

        List<PhoneDataSet> phones = dbServiceHibernate.readPhonesByName(userName);
        System.out.println(userName + " phones from DB:");
        phones.forEach(phone->System.out.println(phone.getNumber()));

        dbServiceHibernate.shutdown();
    }
}
