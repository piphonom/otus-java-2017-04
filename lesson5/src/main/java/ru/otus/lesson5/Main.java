package ru.otus.lesson5;

import ru.otus.lesson5.testunit.*;

/**
 * Created by piphonom on 08.05.17.
 */
public class Main {
    public static void main(String[] args) {
        TestsRunner.run(new Class<?>[] {TestMe.class, TestMeToo.class});
    }

}
