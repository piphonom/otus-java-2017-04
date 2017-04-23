package ru.otus.lesson4;

import org.apache.log4j.Logger;

/**
 * Created by piphonom on 23.04.17.
 */
public class GCStatistic {
    private long totalDuration = 0;
    private long lastDuration = 0;
    private int numberOfCalls = 0;
    private Logger logger;
    private String gcName;

    public GCStatistic(Logger logger) {
        this.logger = logger;
    }

    public void updateTotalDuration(long lastDuration) {
        this.lastDuration = lastDuration;
        totalDuration += lastDuration;
    }

    public void updateNumberOfCalls() {
        numberOfCalls++;
    }

    public void setGCName(String gcName) {
        this.gcName = gcName;
    }

    public void logStatistic() {
        logger.debug(gcName + ": last duration " + lastDuration +
                ", total duration: " + totalDuration +
                ", number of calls: " + numberOfCalls);
    }
}

