package ru.otus.lesson9.base.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by piphonom
 */
@Entity
@Table(name = "User")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    String fullName;

    @Column(name = "age")
    int totalAge;

    public UserDataSet() {
        this.fullName = null;
        this.totalAge = 0;
    }

    public UserDataSet(String name, int age) {
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
}
