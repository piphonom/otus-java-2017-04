package ru.otus.lesson7;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public abstract class ATM {
    protected List<? extends Cell> cells;
    protected int id;
    protected Department department;

    public ATM(int id, List<? extends Cell> cells, Department department) {
        this.id = id;
        this.cells = new ArrayList<>(cells);
        this.department = department;
        if (department != null)
            department.registerATM(this);
    }

    public int getId() {
        return id;
    }

    public void restoreState(Caretaker caretaker) {
        updateState(caretaker.getMemento(id));
    }

    abstract Integer[] getCash(int amount);
    abstract Integer[] addCash(Integer[] banknotes);
    abstract int getRemainder();
    abstract Memento saveState();
    abstract void updateState(Memento memento);

}
