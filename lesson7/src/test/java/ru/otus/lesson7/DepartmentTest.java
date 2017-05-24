package ru.otus.lesson7;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by piphonom
 */
public class DepartmentTest {
    @Test
    public void resetTest() {
        Department department = new Department();
        ATM atmA = new ChainATM.Builder().
                setId(1).
                setDepartment(department).
                addCell(1).addCell(2).addCell(5).addCell(20).addCell(100).
                build();

        ATM atmB = new ChainATM.Builder().
                setId(2).
                setDepartment(department).
                addCell(5).addCell(10).addCell(20).addCell(50).addCell(100).
                build();

        Integer[] bigBundleOfMoney = new Integer[] {100, 100, 100,
                                                  50,  50,  50,
                                                  20,  20,  20,
                                                  10,  10,  10,
                                                   5,   5,   5,
                                                   2,   2,   2,
                                                   1,   1,   1};

        atmA.addCash(bigBundleOfMoney);
        Integer[] anOtherBundleOfMoney = new Integer[] {100, 100, 100,
                                                         20,  20,  20,
                                                         10,  10,  10,
                                                          5,   5,   5};

        bigBundleOfMoney = ArrayUtils.addAll(bigBundleOfMoney, anOtherBundleOfMoney);
        atmB.addCash(bigBundleOfMoney);

        department.reset();

        Assert.assertEquals(0, atmA.getRemainder());
        Assert.assertEquals(0, atmB.getRemainder());
    }

    @Test
    public void remainderTest() {
        Department department = new Department();
        ATM atmA = new ChainATM.Builder().
                setId(1).
                setDepartment(department).
                addCell(1).addCell(2).addCell(5).addCell(20).addCell(100).
                build();

        ATM atmB = new ChainATM.Builder().
                setId(2).
                setDepartment(department).
                addCell(5).addCell(10).addCell(20).addCell(50).addCell(100).
                build();

        Integer[] bigBundleOfMoney = new Integer[] {100, 50, 50, 20,  20, 10, 5, 5, 2, 2, 2, 1};
        atmA.addCash(bigBundleOfMoney);

        Integer[] anOtherBundleOfMoney = new Integer[] {100, 20, 20, 20, 10, 5, 5};

        Assert.assertEquals(atmA.getRemainder() + atmB.getRemainder(), department.getRemainder());
    }
}
