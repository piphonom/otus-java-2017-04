package ru.otus.lesson6;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by piphonom
 */
public class CompositeATMTest {
    @Test
    public void addCashTest() {
        CompositeATM atm = new CompositeATM.Builder().
                addProcessor(new CompositeCell(1)).
                addProcessor(new CompositeCell(5)).
                addProcessor(new CompositeCell(2)).
                addProcessor(new CompositeCell(20)).
                addProcessor(new CompositeCell(10)).
                addProcessor(new CompositeCell(50)).
                addProcessor(new CompositeCell(100)).
                build();
        Integer[] bundleOfMoney = new Integer[] {100, 5, 1, 2, 20, 10, 10, 20, 50, 50, 1, 2};
        Integer[] unknownNotes = atm.addCash(bundleOfMoney);
        Assert.assertTrue(unknownNotes == null);
    }

    @Test
    public void addCashWithUnknownNotesTest() {
        CompositeATM atm = new CompositeATM.Builder().
                addProcessor(new CompositeCell(1)).
                addProcessor(new CompositeCell(5)).
                addProcessor(new CompositeCell(2)).
                addProcessor(new CompositeCell(20)).
                addProcessor(new CompositeCell(10)).
                addProcessor(new CompositeCell(50)).
                addProcessor(new CompositeCell(100)).
                build();
        Integer[] bundleOfMoney = new Integer[] {5, 1, 2, 20, 10, 3, 10, 7, 20, 50, 50, 15, 1, 2};
        Integer[] unknownNotes = atm.addCash(bundleOfMoney);
        Assert.assertTrue(
                ArrayUtils.contains(unknownNotes, 3) &&
                        ArrayUtils.contains(unknownNotes, 7) &&
                        ArrayUtils.contains(unknownNotes, 15)
        );
    }

    @Test
    public void getCashTest() {
        CompositeATM atm = new CompositeATM.Builder().
                addProcessor(new CompositeCell(1)).
                addProcessor(new CompositeCell(5)).
                addProcessor(new CompositeCell(2)).
                addProcessor(new CompositeCell(20)).
                addProcessor(new CompositeCell(10)).
                addProcessor(new CompositeCell(50)).
                addProcessor(new CompositeCell(100)).
                build();
        Integer[] bundleOfMoney = new Integer[] {100, 5, 1, 2, 20, 10, 10, 20, 50, 50, 1, 2};
        atm.addCash(bundleOfMoney);
        Integer[] cash = atm.getCash(177);
        Assert.assertTrue(
                ArrayUtils.contains(cash, 100) &&
                        ArrayUtils.contains(cash, 50) &&
                        ArrayUtils.contains(cash, 20) &&
                        ArrayUtils.contains(cash, 5) &&
                        ArrayUtils.contains(cash, 2)
        );
        Assert.assertEquals(94, atm.getRemainder());
    }

    @Test
    public void noCashTest() {
        CompositeATM atm = new CompositeATM.Builder().
                addProcessor(new CompositeCell(1)).
                addProcessor(new CompositeCell(5)).
                addProcessor(new CompositeCell(2)).
                addProcessor(new CompositeCell(20)).
                addProcessor(new CompositeCell(10)).
                addProcessor(new CompositeCell(50)).
                addProcessor(new CompositeCell(100)).
                build();
        Integer[] bundleOfMoney = new Integer[] {100, 5};
        atm.addCash(bundleOfMoney);
        Integer[] cash = atm.getCash(55);
        Assert.assertNull(cash);
    }
}
