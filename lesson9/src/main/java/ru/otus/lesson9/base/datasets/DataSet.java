package ru.otus.lesson9.base.datasets;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Created by piphonom
 */
@MappedSuperclass
public abstract class DataSet {
    @Id
    @Column(name = "id")
    int id;

    public int getId() {
        return id;
    }
}
