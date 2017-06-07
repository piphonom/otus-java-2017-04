package ru.otus.lesson9.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by piphonom
 */
@Entity
@Table(name = "User")
public class User extends DataSet {

    @Id
    @Column(name = "id")
    int ident;

    @Column(name = "name")
    String fullName;

    @Column(name = "age")
    int totalAge;

    public User() {
        this.fullName = null;
        this.totalAge = 0;
    }

    public User(String name, int age) {
        this.fullName = name;
        this.totalAge = age;
    }

    public void setName(String name) {
        this.fullName = name;
    }

    public void setAge(int age) {
        this.totalAge = age;
    }

    public String getName() {
        return fullName;
    }

    public int getAge() {
        return totalAge;
    }

    public int getId() {
        return ident;
    }

}
