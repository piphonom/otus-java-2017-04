package ru.otus.lesson5.testunit;

/**
 * Created by piphonom on 08.05.17.
 */
public class Assert {
    static public void assertTrue(String message, boolean condition) {
        if (!condition) {
            fail(message);
        }
    }

    static public void assertTrue(boolean condition) {
        assertTrue(null, condition);
    }

    static public void assertFalse(String message, boolean condition) {
        if (condition) {
            fail(message);
        }
    }

    static public void assertFalse(boolean condition) {
        assertFalse(null, condition);
    }

    static public void assertNotNull(String message, Object object) {
        if (object == null) {
            fail(message);
        }
    }

    static public void assertNotNull(Object object) {
        assertNotNull(null, object);
    }

    static public void fail(String message) {
        if (message == null) {
            throw new AssertionError();
        }
        throw new AssertionError(message);
    }
}
