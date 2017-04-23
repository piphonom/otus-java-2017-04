package ru.otus.lesson4;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.apache.log4j.Logger;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.List;

/**
 * Created by piphonom on 23.04.17.
 */
public class GCNotificationListener {

    private GCNotificationListener() {}

    private static boolean isListenerInstalled = false;

    public static void installListener(Logger logger) {
        if (isListenerInstalled) {
            return;
        }
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {

                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {

                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    GCStatistic statistic = (GCStatistic) handback;
                    statistic.setGCName(info.getGcName());
                    statistic.updateNumberOfCalls();
                    long duration = info.getGcInfo().getDuration();
                    statistic.updateTotalDuration(duration);
                    String gctype = info.getGcAction();

                    statistic.logStatistic();

                    /*gcLogger.debug(gctype + ": - "
                            + info.getGcInfo().getId() + ", "
                            + info.getGcName()
                            + " (from " + info.getGcCause() + ") " + duration + " milliseconds");*/
                }
            };

            logger.debug("Registred listener for " + gcbean.getName());
            emitter.addNotificationListener(listener, null, new GCStatistic(logger));

        }
        isListenerInstalled = true;
    }
}
