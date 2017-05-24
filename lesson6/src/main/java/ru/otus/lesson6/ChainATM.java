package ru.otus.lesson6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by piphonom
 */
public class ChainATM implements CashProcessor {
    private final List<ChainCell> cells;

    private ChainATM(List<ChainCell> cells) {
        this.cells = new ArrayList<>(cells);
    }

    @Override
    public Integer[] getCash(int amount) {
        ChainCell cell = cells.get(0);
        try {
            Integer[] cash = cell.getCash(amount);
            return cash;
        } catch (NoCashException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Integer[] addCash(Integer[] banknotes) {
        ChainCell cell = cells.get(0);
        return cell.addCash(banknotes);
    }

    @Override
    public int getRemainder() {
        ChainCell cell = cells.get(0);
        return cell.getRemainder();
    }

    public static class Builder {

        List<ChainCell> builderCells = new ArrayList<>();

        public Builder addCell(int nominal) {
            builderCells.add(new ChainCell(nominal));
            return this;
        }

        public ChainATM build() {
            Collections.sort(builderCells, Collections.reverseOrder());
            Iterator iterator = builderCells.iterator();
            if (iterator.hasNext()) {
                iterator.next();
            }
            for (ChainCell cell : builderCells) {
                if (iterator.hasNext()) {
                    cell.setNext((ChainCell)iterator.next());
                }
            }
            try {
                return new ChainATM(builderCells);
            } finally {
                builderCells.clear();
            }
        }
    }
}
