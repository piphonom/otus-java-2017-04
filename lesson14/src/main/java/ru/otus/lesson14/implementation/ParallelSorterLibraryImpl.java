package ru.otus.lesson14.implementation;

import ru.otus.lesson14.base.ParallelSorter;

import java.util.Arrays;

/**
 * Created by piphonom
 */
public class ParallelSorterLibraryImpl implements ParallelSorter {

    private static final String NAME = "Library ForkJoin sorter";

    @Override
    public <T extends Comparable<? super T>> void sort(T[] toSort) {
        Arrays.parallelSort(toSort);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
