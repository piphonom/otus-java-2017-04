package ru.otus.lesson16.mediator.server;


import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.channel.messages.Msg;
import ru.otus.lesson16.channel.socket.ServerSocketMsgChannel;
import ru.otus.lesson16.channel.socket.SocketChannelRegistry;
import ru.otus.lesson16.channel.socket.SocketChannelRegistryImpl;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by piphonom
 */
public class MediatorServer implements MediatorServerMBean {

    private static final Logger logger = Logger.getLogger(MediatorServer.class.getName());

    private static final int THREADS_NUMBER = 2;
    private static final int PORT = 5050;

    private final ExecutorService executor;
    private final SocketChannelRegistry registry;

    public MediatorServer() {
        executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        registry = new SocketChannelRegistryImpl();
    }

    public void start() throws Exception {
        executor.submit(this::processFrontendIncoming);
        executor.submit(this::processDbServiceIncoming);
        int idCounter = 0;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("Server started on port: " + serverSocket.getLocalPort());

            while (!executor.isShutdown()) {
                Socket client = serverSocket.accept();
                MsgChannel channel = new ServerSocketMsgChannel(client, idCounter++, registry);
                channel.init();
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void processFrontendIncoming() {
        Map<Long, MsgChannel> frontProducers = registry.getProducers();
        Map<Long, MsgChannel> dbConsumers = registry.getConsumers();
        Random randomSource = new Random(System.currentTimeMillis());
        while (true) {
            /*
             * TODO: try with ConcurrentHashMap: no need to use additional synchronized blocks. But only one thread can safely use iterator.
            * */
            Set<Entry<Long, MsgChannel>> frontChannels = frontProducers.entrySet();
            synchronized (frontProducers) {
                for(Entry<Long, MsgChannel> channelEntry : frontChannels) {
                    Msg msg = channelEntry.getValue().poll();
                    if (msg != null && dbConsumers.size() > 0) {
                        while (true) {
                            int bound = dbConsumers.size();
                            int dbServiceNumber = randomSource.nextInt(bound);
                            MsgChannel dbChannel;
                            Set<Long> keys = dbConsumers.keySet();
                            synchronized (dbConsumers) {
                                int counter = 0;
                                Iterator<Long> keysIter = keys.iterator();
                                while (keysIter.hasNext() && counter++ != dbServiceNumber) {
                                    keysIter.next();
                                }
                                dbChannel = dbConsumers.get(keysIter.next());
                            }
                            if (dbChannel != null) {
                                dbChannel.send(msg);
                                break;
                            }
                        }
                    } /*else if (dbConsumers.size() == 0) {
                        logger.log(Level.SEVERE, "Front message process error: no DBServices exists");
                    }*/
                }
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void processDbServiceIncoming() {
        Map<Long, MsgChannel> frontProducers = registry.getProducers();
        Map<Long, MsgChannel> dbConsumers = registry.getConsumers();
        while (true) {
            Set<Entry<Long, MsgChannel>> dbChannels = dbConsumers.entrySet();
            synchronized (dbChannels) {
                for(Entry<Long, MsgChannel> channelEntry : dbChannels) {
                    Msg msg = channelEntry.getValue().poll();
                    if (msg != null) {
                        MsgChannel frontChannel = frontProducers.get(msg.getChannelId());
                        if (frontChannel != null) {
                            frontChannel.send(msg);
                        } else {
                            logger.log(Level.SEVERE, "DB message process error: Front Service with id " + msg.getChannelId() + " is disconnected");
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean getRunning() {
        return true;
    }

    @Override
    public void setRunning(boolean running) {
        if (!running) {
            executor.shutdown();
            logger.info("Bye.");
        }
    }
}
