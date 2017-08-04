package ru.otus.lesson16.dbservice.datasets;

import javax.persistence.*;

/**
 * Created by piphonom
 */
@MappedSuperclass
public abstract class DataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public int getId() {
        return id;
    }

}
