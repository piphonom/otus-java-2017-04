package ru.otus.lesson6;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by piphonom
 */
public class CompositeATM implements CashProcessor {

    private final List<CashProcessor> cells;

    private CompositeATM(List<CashProcessor> cells) {
        this.cells = new ArrayList<>(cells);
    }

    @Override
    public Integer[] getCash(int amount) {

        Integer[] cashToReturn = new Integer[0];
        for (CashProcessor cell : cells) {
            if (amount != 0) {
                Integer[] cash = cell.getCash(amount);
                amount -= ((Cell)cell).getNominal() * cash.length;
                cashToReturn = ArrayUtils.addAll(cashToReturn, cash);
            }
        }
        if (amount != 0) {
            System.out.println("Not enough cash. I have no " + amount + " coins");
            return null;
        } else {
            return cashToReturn;
        }
    }

    @Override
    public Integer[] addCash(Integer[] banknotes) {
        for (CashProcessor cell : cells) {
                banknotes = cell.addCash(banknotes);
        }
        return banknotes;
    }

    @Override
    public int getRemainder() {
        int remainder = 0;
        for (CashProcessor cell : cells)
            remainder += cell.getRemainder();
        return remainder;
    }

    public static class Builder {
        List<CashProcessor> builderCells = new ArrayList<>();

        public Builder addProcessor(CashProcessor processor) {
            builderCells.add(processor);
            return this;
        }

        public CompositeATM build() {
            Collections.sort(builderCells, Collections.reverseOrder());
            try {
                return new CompositeATM(builderCells);
            } finally {
                builderCells.clear();
            }
        }
    }
}
