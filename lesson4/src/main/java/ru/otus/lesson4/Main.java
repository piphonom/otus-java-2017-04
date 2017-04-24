package ru.otus.lesson4;

import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by piphonom on 23.04.17.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yy.MM.dd_HH:mm:ss");
        Logger logger = GCLoggerFactory.getGCLogger("./logs/GCEventsLog_" +
                                                    dateFormat.format(Calendar.getInstance().getTime()) +
                                                    ".txt");

        String[] usedGC = GetGCNames();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("JVM run with explicitly specified Garbage Collectors: ");
        for (String s : usedGC) {
            stringBuilder.append(s + " ");
        }
        logger.debug(stringBuilder.toString());

        GCNotificationListener.installListener(logger);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ChunkSize chunkSizeBean = new ChunkSize();
        ObjectName chunkName = new ObjectName("ru.otus:type=ChunkSize");
        mbs.registerMBean(chunkSizeBean, chunkName);

        SleepTimeout sleepTimeoutBean = new SleepTimeout();
        ObjectName timeoutName = new ObjectName("ru.otus:type=SleepTimeout");
        mbs.registerMBean(sleepTimeoutBean, timeoutName);

        chunkSizeBean.setSize(1_000_000);
        sleepTimeoutBean.setTimeout(10_000);

        BenchMark.run(chunkSizeBean, sleepTimeoutBean, logger);
    }

    private static String[] GetGCNames() {
        List<String> names = new ArrayList<>();

        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        List<String> inputArgs = runtimeMxBean.getInputArguments();
        for (String arg : inputArgs) {
            if (arg.startsWith("-XX:+Use") && arg.endsWith("GC"))
                names.add(arg.substring(8));
        }

        return names.toArray(new String[names.size()]);
    }
}
