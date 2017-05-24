package ru.otus.lesson7;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by piphonom
 */
public class ChainATMTest {
    @Test
    public void addCashTest() {
        ChainATM atm = new ChainATM.Builder().addCell(1).addCell(5).addCell(2).addCell(20).addCell(10).addCell(50).addCell(100).build();
        Integer[] bundleOfMoney = new Integer[] {100, 5, 1, 2, 20, 10, 10, 20, 50, 50, 1, 2};
        Integer[] unknownNotes = atm.addCash(bundleOfMoney);
        Assert.assertTrue(unknownNotes == null);
    }

    @Test
    public void addCashWithUnknownNotesTest() {
        ChainATM atm = new ChainATM.Builder().addCell(1).addCell(5).addCell(2).addCell(20).addCell(10).addCell(50).addCell(100).build();
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
        ChainATM atm = new ChainATM.Builder().addCell(1).addCell(5).addCell(2).addCell(20).addCell(10).addCell(50).addCell(100).build();
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
        ChainATM atm = new ChainATM.Builder().addCell(1).addCell(5).addCell(2).addCell(20).addCell(10).addCell(50).addCell(100).build();
        Integer[] bundleOfMoney = new Integer[] {100, 5};
        atm.addCash(bundleOfMoney);
        Integer[] cash = atm.getCash(55);
        Assert.assertNull(cash);
    }
}
