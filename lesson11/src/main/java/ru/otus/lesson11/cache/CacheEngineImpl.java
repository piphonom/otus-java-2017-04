package ru.otus.lesson11.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * Created by piphonom
 */
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private final int EXEC_EXTRA_DELAY_MS = 5;

    private final Map<K, Element<V>> elements;
    private final boolean isEternal;
    private final long idleTimeout;
    private final long lifetime;
    private int hitCount = 0;
    private int missCount = 0;
    private Timer timer = new Timer();

    public CacheEngineImpl(int maxCapacity, boolean isEternal, long idleTimeout, long maxLifetime) {
        elements = new SoftHashMap<>(maxCapacity);
        this.idleTimeout = idleTimeout > 0 ? idleTimeout : 0;
        this.lifetime = maxLifetime > 0 ? maxLifetime : 0;
        this.isEternal = isEternal || (idleTimeout == 0 && lifetime == 0);
    }

    @Override
    public void put(K key, V value) {
        Element<V> element = new Element<>(value);
        elements.put(key, element);

        System.out.println("Put into cache by key: " + key);

        if (!isEternal) {
            timer.purge();
            if (lifetime != 0) {
                TimerTask task = getTimerTask(key, e -> e.getCreationTime() + lifetime);
                timer.schedule(task, lifetime + EXEC_EXTRA_DELAY_MS);
            }
            if (idleTimeout != 0) {
                TimerTask task = getTimerTask(key, e -> e.getLastAccessTime() + idleTimeout);
                //timer.schedule(task, EXEC_EXTRA_DELAY_MS, idleTimeout);
                timer.scheduleAtFixedRate(task, idleTimeout + EXEC_EXTRA_DELAY_MS, idleTimeout);
            }
        }
    }

    @Override
    public V get(K key) {
        Element<V> element = elements.get(key);
        if (element != null) {
            System.out.println("Got from cache by key: " + key);
            hitCount++;
            element.setAccessed();
            return element.getValue();
        }
        missCount++;
        return null;
    }

    @Override
    public int getHitCount() {
        return hitCount;
    }

    @Override
    public int getMissCount() {
        return missCount;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(K key, Function<Element<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                Element<V> element = elements.get(key);
                if ((element == null) || (timeFunction.apply(element) < System.currentTimeMillis())) {
                    if (element != null) {
                        System.out.println("Removed from cache by key: " + key);
                        //System.out.println("Creation time: " + element.getCreationTime());
                        //System.out.println("Last Accessed time: " + element.getLastAccessTime());
                        //System.out.println("Current time: " + System.currentTimeMillis());
                        elements.remove(key);
                    }
                    cancel();
                }
            }
        };
    }
}
