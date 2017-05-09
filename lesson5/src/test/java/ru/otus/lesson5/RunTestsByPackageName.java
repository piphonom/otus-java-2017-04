package ru.otus.lesson5;

import org.junit.Test;
import ru.otus.lesson5.testunit.TestsRunner;

/**
 * Created by piphonom on 10.05.17.
 */
public class RunTestsByPackageName {
    @Test
    public void runTestsByPackageName() {
        TestsRunner.run("ru.otus.lesson5.tests");
    }
}
