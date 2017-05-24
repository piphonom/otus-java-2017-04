package ru.otus.lesson7;

import java.util.List;

/**
 * Created by piphonom
 */
public final class MementoForChain extends Memento {
    public MementoForChain(List<? extends Cell> cells) {
        super(cells);
    }
}
