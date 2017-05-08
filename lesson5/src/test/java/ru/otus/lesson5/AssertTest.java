package ru.otus.lesson5;

import org.junit.Test;

/**
 * Created by piphonom on 09.05.17.
 */
public class AssertTest {

    @Test
    public void assertTrueTest() {
        ru.otus.lesson5.testunit.Assert.assertTrue("assertTrue test", true);
    }

    @Test
    public void assertFalseTest() {
        ru.otus.lesson5.testunit.Assert.assertFalse("assertFalse test", false);
    }

    @Test
    public void assertNotNullTest() {
        ru.otus.lesson5.testunit.Assert.assertNotNull("assertFalse test", "Not Null");
    }
}
