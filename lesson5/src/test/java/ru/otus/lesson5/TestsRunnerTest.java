package ru.otus.lesson5;

import ru.otus.lesson5.testunit.TestsRunner;
import org.junit.Test;
import org.junit.Assert;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * Created by piphonom on 08.05.17.
 */
public class TestsRunnerTest {

    /*
    * As it's said in different topics on the stackoverflow etc
    * it's not good idea to test private methods of the class.
    * But to work with reflection it's a good point to make it.
    * */

    @Test
    public void getBeforeMethodTest () {
        try {
            TestsRunner runner = new TestsRunner();
            Method getBefore = runner.getClass().getDeclaredMethod("getBeforeMethod", Method[].class);
            getBefore.setAccessible(true);
            Method[] victimMethods = Victim.class.getDeclaredMethods();
            Method method = (Method) getBefore.invoke(runner, new Object[] {victimMethods});
            Assert.assertEquals(Victim.class.getDeclaredMethod("beforeMethod"), method);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getAfterMethodTest () {
        try {
            TestsRunner runner = new TestsRunner();
            Method getAfter = runner.getClass().getDeclaredMethod("getAfterMethod", Method[].class);
            getAfter.setAccessible(true);
            Method[] victimMethods = Victim.class.getDeclaredMethods();
            Method method = (Method) getAfter.invoke(runner, new Object[] {victimMethods});
            Assert.assertEquals(Victim.class.getDeclaredMethod("afterMethod"), method);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void getTestMethodsTest () {
        try {
            TestsRunner runner = new TestsRunner();
            Method getTests = runner.getClass().getDeclaredMethod("getTestMethods", Method[].class);
            getTests.setAccessible(true);
            Method[] victimMethods = Victim.class.getDeclaredMethods();
            Method[] methods = (Method[]) getTests.invoke(runner, new Object[] {victimMethods});
            List<Method> methodsList = Arrays.asList(methods);
            Assert.assertTrue(methodsList.contains(Victim.class.getDeclaredMethod("test1")));
            Assert.assertTrue(methodsList.contains(Victim.class.getDeclaredMethod("test2")));
            Assert.assertTrue(methodsList.contains(Victim.class.getDeclaredMethod("test3")));
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Assert.fail(e.getMessage());
        }
    }

    class Victim {

        @ru.otus.lesson5.testunit.Before
        void beforeMethod() {}

        @ru.otus.lesson5.testunit.After
        void afterMethod() {}

        @ru.otus.lesson5.testunit.Test
        void test1() {}

        @ru.otus.lesson5.testunit.Test
        void test2() {}

        @ru.otus.lesson5.testunit.Test
        void test3() {}
    }
}
