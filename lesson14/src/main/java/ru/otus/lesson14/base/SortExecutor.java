package ru.otus.lesson14.base;

import java.util.Arrays;

/**
 * Created by piphonom
 */
public class SortExecutor {

    private ParallelSorter sorter;

    public SortExecutor(ParallelSorter sorter) {
        this.sorter = sorter;
    }

    public <T extends Comparable<? super T>> void exec(T[] toSort) {

        System.out.println("\nUsing sorter: " + sorter.getName());
        System.out.println("Array to sort: " + Arrays.toString(toSort));
        long startTime = System.currentTimeMillis();
        sorter.sort(toSort);
        long finishTime = System.currentTimeMillis();
        System.out.println("Sorted  array: " + Arrays.toString(toSort));
        System.out.println("Time to sort: " + (finishTime - startTime) + "(ms)");
    }
}
