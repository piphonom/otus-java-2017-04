package ru.otus.lesson14;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.lesson14.base.SortExecutor;
import ru.otus.lesson14.implementation.ParallelSorterForkJoinImpl;
import ru.otus.lesson14.implementation.ParallelSorterLibraryImpl;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by piphonom
 */
public class SortTest {

    private static int ARRAY_SIZE = 2000;

    @Test
    public void Sort() {
        Integer source[] = new Integer[ARRAY_SIZE];
        generateIntegerSource(source);
        Integer[] sourceClone = Arrays.copyOf(source, source.length);

        SortExecutor executorOne = new SortExecutor(new ParallelSorterLibraryImpl());
        executorOne.exec(source);

        SortExecutor executorTwo = new SortExecutor(new ParallelSorterForkJoinImpl());
        executorTwo.exec(sourceClone);

        Assert.assertTrue(Arrays.equals(source, sourceClone));
    }

    private static void generateIntegerSource(Integer[] source) {
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < source.length; i++) {
            source[i] = random.nextInt(100);
        }
    }
}
