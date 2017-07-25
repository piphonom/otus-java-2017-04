package ru.otus.lesson14.implementation;

import ru.otus.lesson14.base.ParallelSorter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Created by piphonom
 */
public class ParallelSorterForkJoinImpl implements ParallelSorter {

    private static int CHUNK_THRESHOLD = 10;

    private static final String NAME = "Own ForkJoin sorter";

    @Override
    public <T extends Comparable<? super T>> void sort(T[] toSort) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        forkJoinPool.invoke(new SortTask<>(toSort, 0, toSort.length));
    }

    @Override
    public String getName() {
        return NAME;
    }

    private class SortTask<T extends Comparable<? super T>> extends RecursiveAction {

        final T[] array;
        final T[] helper;
        final int lo;
        final int hi;

        SortTask(T[] array, int lo, int hi) {
            this.array = array;
            this.helper = (T[])Array.newInstance(array[0].getClass(), array.length);
            this.lo = lo;
            this.hi = hi;
        }

        protected void compute() {
            if ((hi - lo) < CHUNK_THRESHOLD) {
                    Arrays.sort(array, lo, hi);
            } else {
                int mid = (lo + hi) >>> 1;
                invokeAll(new SortTask(array, lo, mid),
                        new SortTask(array, mid, hi));
                    merge(array, lo, hi, mid);
            }
        }

        private void merge(T[] array, int lo, int hi, int mid) {

            int i = lo;
            int j = mid;
            int k = lo;

            System.arraycopy(array, lo, helper, lo, hi - lo);
            while(i < mid && j < hi) {
                if (helper[i].compareTo(helper[j]) <= 0) {
                    array[k++] = helper[i++];
                } else {
                    array[k++] = helper[j++];
                }
            }
            while (i < mid) {
                array[k++] = helper[i++];
            }
        }
    }
}
