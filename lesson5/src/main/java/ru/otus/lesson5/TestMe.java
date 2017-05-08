package ru.otus.lesson5;

import ru.otus.lesson5.testunit.After;
import ru.otus.lesson5.testunit.Assert;
import ru.otus.lesson5.testunit.Before;
import ru.otus.lesson5.testunit.Test;

/**
 * Created by piphonom on 09.05.17.
 */
public class TestMe {
    @Before
    void before() {
        System.out.println("I'm before() of TestMe class");
    }

    @After
    void after() {
        System.out.println("I'm after() of TestMe class");
    }

    @Test
    void test1() {
        System.out.println("I'm test1() of TestMe class");
        Assert.assertFalse(false);
    }

    @Test
    void test2() {
        System.out.println("I'm test2() of TestMe class");
        Assert.assertTrue(true);
    }
}
