package ru.otus.lesson13.cache;

/**
 * Created by piphonom
 */
public interface CacheEngine<K, V> {
    void put(K key, V value);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

    void printInfo();
}
