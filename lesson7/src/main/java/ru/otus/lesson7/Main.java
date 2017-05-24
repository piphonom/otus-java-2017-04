package ru.otus.lesson7;

/**
 * Created by piphonom
 */
public class Main {
    public static void main(String[] args) {
        Department department = new Department();
        ATM atmA = new ChainATM.Builder().
                setId(1).
                setDepartment(department).
                addCell(1).addCell(2).addCell(5).addCell(20).addCell(50).addCell(100).
                build();

        System.out.println("Created ATM_A with initial amount: " + atmA.getRemainder());

        ATM atmB = new ChainATM.Builder().
                setId(2).
                setDepartment(department).
                addCell(5).addCell(10).addCell(20).addCell(50).addCell(100).
                build();

        System.out.println("Created ATM_B with initial amount: " + atmB.getRemainder());

        System.out.println("Total amount of department: " + department.getRemainder());

        Integer[] bigBundleOfMoney = new Integer[] {100, 100, 100,
                                                     50,  50,  50,
                                                     20,  20,  20,
                                                     10,  10,  10,
                                                      5,   5,   5,
                                                      2,   2,   2,
                                                      1,   1,   1};

        atmA.addCash(bigBundleOfMoney);
        System.out.println("Added cash into ATM_A, new amount: " + atmA.getRemainder());

        atmB.addCash(bigBundleOfMoney);
        System.out.println("Added cash into ATM_B, new amount: " + atmB.getRemainder());

        System.out.println("Total department amount: " + department.getRemainder());

        department.reset();
        System.out.println("After reset of whole department: ATM_A ammount " + atmA.getRemainder() +
                ", ATM_B amount " + atmB.getRemainder() +
                ", department amount " + department.getRemainder());
    }

}
