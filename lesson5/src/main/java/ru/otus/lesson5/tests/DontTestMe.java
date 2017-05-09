package ru.otus.lesson5.tests;

import ru.otus.lesson5.testunit.Assert;
import ru.otus.lesson5.testunit.Test;

/**
 * Created by piphonom on 10.05.17.
 */
public class DontTestMe {
    @Test
    void test() {
        System.out.println("I shouldn't be started");
        Assert.assertTrue(false);
    }
}
