package ru.otus.lesson5;

import org.junit.Ignore;
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
    @Ignore
    public void failedAssertTrueTest() {
        ru.otus.lesson5.testunit.Assert.assertTrue("Failed assertTrue test", false);
    }

    @Test
    public void assertFalseTest() {
        ru.otus.lesson5.testunit.Assert.assertFalse("assertFalse test", false);
    }

    @Test
    @Ignore
    public void failedAssertFalseTest() {
        ru.otus.lesson5.testunit.Assert.assertFalse("Failed assertFalse test", true);
    }

    @Test
    public void assertNotNullTest() {
        ru.otus.lesson5.testunit.Assert.assertNotNull("assertNotNull test", "Not Null");
    }

    @Test
    @Ignore
    public void failedAssertNotNullTest() {
        ru.otus.lesson5.testunit.Assert.assertNotNull("Failed assertNotNull test", null);
    }
}
