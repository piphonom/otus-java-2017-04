package ru.otus.lesson11;

import ru.otus.lesson11.base.DBService;
import ru.otus.lesson11.base.datasets.AddressDataSet;
import ru.otus.lesson11.base.datasets.PhoneDataSet;
import ru.otus.lesson11.base.datasets.UserDataSet;
import ru.otus.lesson11.implementations.hibernate.DBServiceHibernateImpl;
import ru.otus.lesson11.implementations.myorm.DBServiceImpl;
import ru.otus.lesson11.myorm.connectors.MySqlSimpleConnector;

import java.util.List;

/**
 * Created by piphonom
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        /* My ORM */
        System.out.println("My ORM:");
        DBService dbService = new DBServiceImpl(MySqlSimpleConnector::new);

        /*
        UserDataSet firstUser = new UserDataSet("Michael Jackson", 25);
        if (dbService.save(firstUser)) {
            System.out.println("Michael Jackson is saved");
        }

        UserDataSet dbUser = dbService.read(1);
        System.out.println("UserDataSet by id - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());
        dbUser = dbService.readByName("Michael Jackson");
        System.out.println("UserDataSet by name - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        Thread.sleep(10_000);

        dbUser = dbService.read(1);
        System.out.println("UserDataSet by id - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());
        dbUser = dbService.readByName("Michael Jackson");
        System.out.println("UserDataSet by name - id: " + dbUser.getId() + ", name: " + dbUser.getName() + ", age: " + dbUser.getAge());

        dbService.shutdown();
        */

        while (true) {
            Thread.sleep(5_000);
            dbService.printInfo();
        }

        /* Hibernate */
        /*
        System.out.println("\nHibernate:");
        DBService dbServiceHibernate = new DBServiceHibernateImpl();

        String status = dbServiceHibernate.getLocalStatus();
        System.out.println("Status: " + status);

        String userName = "Mickey Mouse";
        UserDataSet mickey = new UserDataSet(userName, 89);
        mickey.setPhone(new PhoneDataSet("12345"));
        mickey.setPhone(new PhoneDataSet("67890"));
        mickey.setAddress(new AddressDataSet("123", "Disney Land"));

        dbServiceHibernate.save(mickey);
        System.out.println(userName + " is saved");

        List<PhoneDataSet> phones = dbServiceHibernate.readPhonesByName(userName);
        System.out.println(userName + " phones from DB:");
        phones.forEach(phone->System.out.println(phone.getNumber()));

        dbServiceHibernate.shutdown();
        */
    }
}
