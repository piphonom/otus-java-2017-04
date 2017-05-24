package ru.otus.lesson7;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by piphonom
 */
public class Caretaker {
    private Map<Integer, Memento> mementoMap = new HashMap<>();

    public void addMemento(Integer atmId, Memento memento) {
        mementoMap.put(atmId, memento);
    }

    public Memento getMemento(Integer atmId) {
        return mementoMap.get(atmId);
    }
}
