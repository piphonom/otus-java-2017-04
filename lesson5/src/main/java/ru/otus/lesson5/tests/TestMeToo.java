package ru.otus.lesson5.tests;

import ru.otus.lesson5.testunit.*;

/**
 * Created by piphonom on 09.05.17.
 */
@TestClass
public class TestMeToo {
    @Before
    void before() {
        System.out.println("I'm before() of TestMeToo class");
    }

    @After
    void after() {
        System.out.println("I'm after() of TestMeToo class");
    }

    @Test
    void test1() {
        System.out.println("I'm test1() of TestMeToo class");
        Assert.assertNotNull(1);
    }

    @Test
    void test2() {
        System.out.println("I'm test2() of TestMeToo class");
        Assert.assertNotNull("message", "Object");
    }
}
