package ru.otus.lesson6;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public class CompositeCell extends Cell implements CashProcessor {
    public CompositeCell(int nominal) {
        super(nominal);
    }

    @Override
    public Integer[] getCash(int desired) {
        int notesNumber = Math.min(desired / nominal, cash.size());

        List<Integer> notes = new ArrayList<>();
        notes.addAll(cash.subList(cash.size() - notesNumber, cash.size()));
        cash.subList(cash.size() - notesNumber, cash.size()).clear();
        return notes.toArray(new Integer[notes.size()]);
    }

    @Override
    public Integer[] addCash(Integer[] banknotes) {
        if (banknotes == null)
            return null;
        if (banknotes.length == 0)
            return null;
        List<Integer> newNotes = new ArrayList<>();
        for (Integer note : banknotes) {
            if (note == nominal) {
                cash.add(note);
                newNotes.add(note);
            }
        }
        banknotes = ArrayUtils.removeElements(banknotes, newNotes.toArray(new Integer[newNotes.size()]));
        return banknotes.length != 0 ? banknotes : null;
    }

    @Override
    public int getRemainder() {
        return cash.size() * nominal;
    }
}
