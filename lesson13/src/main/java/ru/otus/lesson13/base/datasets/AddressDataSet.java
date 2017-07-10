package ru.otus.lesson13.base.datasets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by piphonom
 */
@Entity
@Table(name = "Address")
public class AddressDataSet extends DataSet {

    @Column(name = "mailIndex")
    private String mailIndex;

    @Column(name = "address")
    private String address;

    @OneToOne
    private UserDataSet user;

    public AddressDataSet() {}

    public AddressDataSet(String index, String address) {
        this.mailIndex = index;
        this.address = address;
    }

    public void setMailIndex(String mailIndex) {
        this.mailIndex = mailIndex;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUser(UserDataSet user) {
        this.user = user;
    }

    public String getMailIndex() {
        return mailIndex;
    }

    public String getAddress() {
        return address;
    }

    public UserDataSet getUser() {
        return user;
    }
}
