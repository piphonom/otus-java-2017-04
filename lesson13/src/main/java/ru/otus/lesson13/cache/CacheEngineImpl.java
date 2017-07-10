package ru.otus.lesson13.cache;

import ru.otus.lesson13.webserver.annotations.WebAccessed;
import ru.otus.lesson13.webserver.annotations.WebModified;
import ru.otus.lesson13.webserver.annotations.WebSettings;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

/**
 * Created by piphonom
 */
@WebSettings(name = "Cache Engine Settings")
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private final int EXEC_EXTRA_DELAY_MS = 5;

    private transient final Map<K, Element<V>> elements;
    @WebAccessed(name = "isEternal")
    private final boolean isEternal;
    @WebModified(name = "idleTimeout")
    private Long idleTimeout;
    @WebModified(name = "lifetime")
    private Long lifetime;
    @WebAccessed(name = "hitCount")
    private Integer hitCount = 0;
    @WebAccessed(name = "missCount")
    private Integer missCount = 0;
    private transient Timer timer = new Timer();

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

    @Override
    public void printInfo() {
        System.out.println("isEternal: " + isEternal);
        System.out.println("Idle timeout: " + idleTimeout);
        System.out.println("Life time: " + lifetime);
        System.out.println("Hit count: " + hitCount);
        System.out.println("Miss count: " + missCount);
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

    public void IncrementHit() {
        hitCount++;
    }
}
