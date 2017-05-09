package ru.otus.lesson5;

import org.junit.Test;
import ru.otus.lesson5.tests.TestMe;
import ru.otus.lesson5.tests.TestMeToo;
import ru.otus.lesson5.testunit.TestsRunner;

/**
 * Created by piphonom on 10.05.17.
 */
public class RunTestsByArray {
    @Test
    public void runTestsByArray() {
        TestsRunner.run(new Class<?>[] {TestMe.class, TestMeToo.class});
    }
}
