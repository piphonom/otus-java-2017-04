package ru.otus.lesson16.mediator;

import ru.otus.lesson16.mediator.server.MediatorServer;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tully.
 */
public class MediatorMain {
    private static final Logger logger = Logger.getLogger(MediatorMain.class.getName());

    private static final String WEB_CLIENT_START_COMMAND_1 = "java -jar ../WebFrontend/target/web-service.jar -p 8080";
    private static final String WEB_CLIENT_START_COMMAND_2 = "java -jar ../WebFrontend/target/web-service.jar -p 8081";
    private static final String DB_CLIENT_START_COMMAND = "java -jar ../DbService/target/db-service.jar";
    private static final int CLIENT_START_DELAY_SEC = 5;
    private static final int WORKERS_COUNT = 4;

    public static void main(String[] args) throws Exception {
        new MediatorMain().start();
    }

    private void start() throws Exception {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(WORKERS_COUNT);
        startClient(executorService, DB_CLIENT_START_COMMAND);
        startClient(executorService, DB_CLIENT_START_COMMAND);
        startClient(executorService, WEB_CLIENT_START_COMMAND_1);
        startClient(executorService, WEB_CLIENT_START_COMMAND_2);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("ru.otus:type=Mediator");
        MediatorServer server = new MediatorServer();
        mbs.registerMBean(server, name);

        server.start();

        executorService.shutdown();
    }

    private void startClient(ScheduledExecutorService executorService, String command) {
        executorService.schedule(() -> {
            try {
                new ProcessRunnerImpl().start(command);
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }, CLIENT_START_DELAY_SEC, TimeUnit.SECONDS);
    }

}
