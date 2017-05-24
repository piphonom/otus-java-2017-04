package ru.otus.lesson7;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public class Cell implements Comparable<Cell>{
    protected final int nominal;
    protected List<Integer> cash;

    public Cell(int nominal) {
        cash = new ArrayList<>();
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

    @Override
    public int compareTo(Cell o) {
        return new Integer(nominal).compareTo(new Integer(o.getNominal()));
    }
}
