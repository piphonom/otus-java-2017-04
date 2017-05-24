package ru.otus.lesson6;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by piphonom
 */
public class ChainCell extends Cell implements Iterable<ChainCell> {

    ChainCell next;

    public ChainCell(int nominal) {
        super(nominal);
        next = null;
    }

    public void setNext(ChainCell next) {
        this.next = next;
    }

    public Integer[] getCash(int desired) throws NoCashException {
        int notesNumber = Math.min(desired / nominal, cash.size());

        List<Integer> notes = new ArrayList<>();
        notes.addAll(cash.subList(cash.size() - notesNumber, cash.size()));
        cash.subList(cash.size() - notesNumber, cash.size()).clear();
        int remainder = desired - (notesNumber * nominal);
        if (remainder > 0) {
            if (next == null) {
                throw new NoCashException("Not enough cash. I have no " + remainder + " coins");
            } else {
                Collections.addAll(notes, next.getCash(remainder));
            }
        }

        return notes.toArray(new Integer[notes.size()]);
    }

    public Integer[] addCash(Integer[] banknotes) {
        if (banknotes.length == 0)
            return null;
        List<Integer> newNotes = new ArrayList<>();
        for (Integer note : banknotes) {
            if (note == nominal) {
                cash.add(note);
                newNotes.add(note);
            }
        }

        Integer[] remainingCash = ArrayUtils.removeElements(banknotes, newNotes.toArray(new Integer[newNotes.size()]));
        if (remainingCash.length != 0) {
            if (next != null) {
                return next.addCash(remainingCash);
            } else {
                return remainingCash;
            }
        } else {
            return null;
        }
    }

    public int getRemainder() {
        int remain = cash.size() * nominal;
        if (next != null)
            remain += next.getRemainder();
        return remain;
    }

    @Override
    public Iterator<ChainCell> iterator() {
        return new CellIterator();
    }

    public class CellIterator implements Iterator<ChainCell> {
        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public ChainCell next() {
            return next;
        }
    }
}
