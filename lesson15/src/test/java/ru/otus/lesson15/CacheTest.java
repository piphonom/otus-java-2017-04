package ru.otus.lesson15;

import org.junit.Assert;
import org.junit.Test;
import ru.otus.lesson15.cache.CacheEngineImpl;
import ru.otus.lesson15.cache.CacheEngine;

/**
 * Created by piphonom
 */
public class CacheTest {
    @Test
    public void EternalTest(){
        final int maxCapacity = 2;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(maxCapacity, true, 0, 0);
        for(int i = 0; i < 2 * maxCapacity; i++) {
            cache.put(i, "String number " + i);
        }
        for (int i = 0; i < maxCapacity; i++) {
            Assert.assertNull(cache.get(i));
        }
        for (int i = maxCapacity; i < 2 * maxCapacity; i++) {
            Assert.assertEquals("String number " + i, cache.get(i));
        }
    }

    @Test
    public void LifetimeTest() throws InterruptedException {
        final int maxCapacity = 2;
        final int lifetime = 1_000;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(maxCapacity, false, 0, lifetime);
        for(int i = 0; i < maxCapacity; i++) {
            cache.put(i, "String number " + i);
        }
        for (int i = 0; i < maxCapacity; i++) {
            Assert.assertEquals("String number " + i, cache.get(i));
        }
        Thread.sleep(2 * lifetime);
        for (int i = 0; i < maxCapacity; i++) {
            Assert.assertNull(cache.get(i));
        }
    }

    @Test
    public void IdleTest() throws InterruptedException {
        final int maxCapacity = 2;
        final int idletime = 1_000;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(maxCapacity, false, idletime, 0);
        for(int i = 0; i < maxCapacity; i++) {
            cache.put(i, "String number " + i);
        }
        Thread.sleep(idletime/2);
        for (int i = 0; i < maxCapacity; i++) {
            Assert.assertEquals("String number " + i, cache.get(i));
        }
        Thread.sleep(2 * idletime);
        for (int i = 0; i < maxCapacity; i++) {
            Assert.assertNull(cache.get(i));
        }
    }
}
