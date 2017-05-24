package ru.otus.lesson7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by piphonom
 */
public final class ChainATM extends ATM {
    private ChainATM(int id, List<? extends Cell> cells, Department department) {
        super(id, cells, department);
    }

    @Override
    public Integer[] getCash(int amount) {
        ChainCell cell = (ChainCell)cells.get(0);
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
        ChainCell cell = (ChainCell)cells.get(0);
        return cell.addCash(banknotes);
    }

    @Override
    public int getRemainder() {
        ChainCell cell = (ChainCell)cells.get(0);
        return cell.getRemainder();
    }

    @Override
    Memento saveState() {
        return new MementoForChain(copyCells(this.cells));
    }

    @Override
    void updateState(Memento memento) {
        if (memento instanceof MementoForChain)
            this.cells = copyCells(memento.getCells());
    }

    private List<? extends Cell> copyCells (List<? extends Cell> cellsToCopy) {
        List<ChainCell> newCells = new ArrayList<>();
        ChainCell cell = (ChainCell) cellsToCopy.get(0);
        ChainCell copyCell = new ChainCell(cell.getNominal());
        while (cell != null) {
            try {
                Integer[] cash = cell.getCash(cell.getRemainder());
                copyCell.addCash(cash);
            } catch (NoCashException e) {
                e.printStackTrace();
            }

            copyCell.next = (cell.next != null ? new ChainCell(cell.next.getNominal()) : null);
            newCells.add(copyCell);

            cell = cell.next;
            copyCell = copyCell.next;
        }
        return newCells;
    }

    public static class Builder {

        private List<ChainCell> builderCells = new ArrayList<>();
        private int id = 0;
        private Department department = null;


        public Builder addCell(int nominal) {
            builderCells.add(new ChainCell(nominal));
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDepartment(Department department) {
            this.department = department;
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
                return new ChainATM(id, builderCells, department);
            } finally {
                builderCells.clear();
            }
        }
    }
}
