package ru.otus.lesson13.cache;

/**
 * Created by piphonom
 */
public class Element<V> {
    private final V value;
    private final long creationTime;
    private long lastAccessTime;

    public Element(V value) {
        this.value = value;
        creationTime = getCurrentTime();
        lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public V getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }
}
