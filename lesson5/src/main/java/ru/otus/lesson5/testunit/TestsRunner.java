package ru.otus.lesson5.testunit;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
            if (c.getAnnotation(TestClass.class) == null) {
                continue;
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

    public static void run(String packageName) {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        run(classes.toArray(new Class<?>[classes.size()]));
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
