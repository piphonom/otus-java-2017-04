package ru.otus.lesson9.base.datasets;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
@Entity
@Table(name = "User")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    String name;

    @Column(name = "age")
    int age;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<PhoneDataSet> phones = new ArrayList<>();

    /*
    @OneToOne(cascade = CascadeType.ALL)
    private PhoneDataSet phone;
    */

    public List<PhoneDataSet> getPhones() { return phones; }


    public UserDataSet() {}

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, List<PhoneDataSet> phones) {
        this.name = name;
        this.phones = phones;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(PhoneDataSet phone) {
        phones.add(phone);
        phone.setUser(this);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
