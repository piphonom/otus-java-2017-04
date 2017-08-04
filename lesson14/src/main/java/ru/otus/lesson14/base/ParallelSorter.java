package ru.otus.lesson14.base;

/**
 * Created by piphonom
 */
public interface ParallelSorter {
    <T extends Comparable<? super T>> void sort(T[] toSort);
    String getName();
}
