package ru.otus.lesson5.testunit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by piphonom on 08.05.17.
 */
public class TestsRunner {
    public static void run(Class<?>[] klasses){
        TestsRunner runner = new TestsRunner();
        for (Class<?> c : klasses) {
            if (c.isPrimitive()) {
                Assert.fail("Primitive types can't be tested");
            }

            Method[] methods = c.getDeclaredMethods();
            Method before = runner.getBeforeMethod(methods);
            Method after = runner.getAfterMethod(methods);
            Method[] testMethods = runner.getTestMethods(methods);
            for (Method test : testMethods) {
                runner.runTest(c, before, test, after);
            }
        }
    }

    private Method getBeforeMethod(Method[] methods) {
        for (Method m : methods) {
            if (m.getAnnotation(Before.class) != null)
                return m;
        }
        return null;
    }

    private Method getAfterMethod(Method[] methods) {
        for (Method m : methods) {
            if (m.getAnnotation(After.class) != null)
                return m;
        }
        return null;
    }

    private Method[] getTestMethods(Method[] methods) {
        ArrayList<Method> methodsList = new ArrayList<>();
        for (Method m : methods) {
            if (m.getAnnotation(Test.class) != null)
                methodsList.add(m);
        }
        return methodsList.toArray(new Method[methodsList.size()]);
    }

    private void runTest(Class<?> klass, Method before, Method test, Method after) {
        try {
            Object testObject = klass.newInstance();
            if (before != null) {
                before.setAccessible(true);
                before.invoke(testObject);
            }
            test.setAccessible(true);
            test.invoke(testObject);
            if (after != null) {
                after.setAccessible(true);
                after.invoke(testObject);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
