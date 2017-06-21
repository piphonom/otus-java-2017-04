package ru.otus.lesson11.base.datasets;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
@Entity
@Table(name = "User")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "User")
public class UserDataSet extends DataSet {

    @Column(name = "name")
    String name;

    @Column(name = "age")
    int age;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "Phone",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private List<PhoneDataSet> phones = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "Address",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private AddressDataSet address;

    public UserDataSet() {}

    public UserDataSet(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public UserDataSet(String name, List<PhoneDataSet> phones) {
        this.name = name;
        this.phones = phones;
        phones.forEach(phone -> phone.setUser(this));
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

    public void setAddress(AddressDataSet address) {
        this.address = address;
        address.setUser(this);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public List<PhoneDataSet> getPhones() { return phones; }

    public AddressDataSet getAddress() {
        return address;
    }
}
