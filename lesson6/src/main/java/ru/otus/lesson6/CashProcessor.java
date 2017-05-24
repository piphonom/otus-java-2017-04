package ru.otus.lesson6;

/**
 * Created by piphonom
 */
public interface CashProcessor {
    Integer[] getCash(int amount);
    Integer[] addCash(Integer[] banknotes);
    int getRemainder();
}
