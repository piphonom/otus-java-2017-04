package ru.otus.lesson7;

import java.util.List;

/**
 * Created by piphonom
 */
public abstract class Memento {
    List<? extends Cell> cells;

    public Memento(List<? extends Cell> cells) {
        this.cells = cells;
    }

    public List<? extends Cell> getCells() {
        return cells;
    }
}
