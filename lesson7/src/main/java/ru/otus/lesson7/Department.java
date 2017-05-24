package ru.otus.lesson7;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public class Department {
    private List<ATM> atms = new ArrayList<>();
    private Caretaker refueler = new Caretaker();

    public void registerATM(ATM atm) {
        atms.add(atm);
        refueler.addMemento(atm.getId(), atm.saveState());
    }

    public void reset() {
        for (ATM atm : atms) {
            atm.restoreState(refueler);
        }
    }

    public int getRemainder() {
        int remainder = 0;
        for (ATM atm : atms) {
            remainder += atm.getRemainder();
        }
        return remainder;
    }
}
