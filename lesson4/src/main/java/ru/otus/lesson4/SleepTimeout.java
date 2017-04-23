package ru.otus.lesson4;

/**
 * Created by lesha on 23.04.17.
 */
public class SleepTimeout implements SleepTimeoutMBean {
    private int timeout;

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
