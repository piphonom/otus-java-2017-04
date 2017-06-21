package ru.otus.lesson11.cache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.lang.ref.*;
import java.util.*;

/**
 * Created by piphonom
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V> {

    private final Map<K, SoftReference<V>> refs = new LinkedHashMap<>();

    private final Map<SoftReference<V>, K> reverseLookup = new HashMap<>();

    private final ReferenceQueue<V> clearedRefsQueue = new ReferenceQueue<>();

    private final int maxElements;

    private final Object lock = new Object();

    public SoftHashMap(int maxElements) {
        this.maxElements = maxElements;
    }

    public V get(Object key) {
        expungeStaleEntries();
        V result = null;
        SoftReference<V> softRef = refs.get(key);
        if (softRef != null) {
            result = softRef.get();
            if (result == null) {
                synchronized (lock) {
                    refs.remove(key);
                    reverseLookup.remove(softRef);
                }
            }
        }
        return result;
    }

    private void expungeStaleEntries() {
        Reference<? extends V> sv;
        while ((sv = clearedRefsQueue.poll()) != null) {
            synchronized (lock) {
                refs.remove(reverseLookup.remove(sv));
            }
        }
    }

    public V put(K key, V value) {
        expungeStaleEntries();
        if (refs.size() == maxElements) {
            K firstKey = refs.keySet().iterator().next();
            SoftReference<V> softRef = refs.get(firstKey);
            if (softRef != null) {
                synchronized (lock) {
                    refs.remove(firstKey);
                    reverseLookup.remove(softRef);
                }
            }
        }
        SoftReference<V> softRef = new SoftReference<>(value, clearedRefsQueue);
        reverseLookup.put(softRef, key);
        SoftReference<V> result = refs.put(key, softRef);
        if (result == null) return null;
        reverseLookup.remove(result);
        return result.get();
    }

    public V remove(Object key) {
        expungeStaleEntries();
        SoftReference<V> result;
        synchronized (lock) {
             result = refs.remove(key);
        }
        if (result == null) return null;
        return result.get();
    }

    public void clear() {
        refs.clear();
        reverseLookup.clear();
    }

    public int size() {
        expungeStaleEntries();
        return refs.size();
    }

    public Set<Entry<K,V>> entrySet() {
        expungeStaleEntries();
        Set<Entry<K,V>> result = new LinkedHashSet<>();
        for (final Entry<K, SoftReference<V>> entry : refs.entrySet()) {
            final V value = entry.getValue().get();
            if (value != null) {
                result.add(new Entry<K, V>() {
                    public K getKey() {
                        return entry.getKey();
                    }
                    public V getValue() {
                        return value;
                    }
                    public V setValue(V v) {
                        entry.setValue(new SoftReference<>(v, clearedRefsQueue));
                        return value;
                    }
                });
            }
        }
        return result;
    }
}
