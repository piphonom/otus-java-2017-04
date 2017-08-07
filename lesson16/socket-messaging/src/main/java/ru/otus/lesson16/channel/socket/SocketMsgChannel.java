package ru.otus.lesson16.channel.socket;


import ru.otus.lesson16.channel.ChannelType;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.channel.messages.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by piphonom
 */
public abstract class SocketMsgChannel implements MsgChannel {
    static final Logger logger = Logger.getLogger(SocketMsgChannel.class.getName());

    private static final int WORKERS_COUNT = 2;
    long id;
    ChannelType type;

    private final BlockingQueue<Msg> outputMessages = new LinkedBlockingQueue<>();
    private final BlockingQueue<Msg> inputMessages = new LinkedBlockingQueue<>();

    private final ExecutorService executor;

    final Socket socket;

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    SocketMsgChannel(Socket socket, long id) throws IOException {
        this.socket = socket;
        this.id = id;
        this.executor = Executors.newFixedThreadPool(WORKERS_COUNT);
    }

    abstract void initStreams() throws IOException;

    @Override
    public Queue<Future<Boolean>> init() {
        try {
            initStreams();
        } catch (IOException e) {
            return null;
        }
        if (!handShake()) {
            try {
                inputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return null;
            }
        }
        Queue<Future<Boolean>> tasks = new ArrayDeque<>(WORKERS_COUNT);
        tasks.add(executor.submit(this::receiveMessage));
        tasks.add(executor.submit(this::sendMessage));

        return tasks;
    }

    abstract boolean handShake();

    public void safeStop() {
        try {
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean receiveMessage() {
        try {
            Msg msg;
            while((msg = (Msg)inputStream.readObject()) != null) {
                inputMessages.add(msg);
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Receive message failed: " + e.getMessage());
            try {
                inputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            safeStop();
        }
        return true;
    }

    private boolean sendMessage() {
        try {
            while (socket.isConnected()) {
                Msg msg = outputMessages.take();
                if (msg.getChannelId() == -1) {
                    msg.setChannelId(id);
                }
                outputStream.writeObject(msg);
            }
        } catch (InterruptedException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            try {
                outputStream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            safeStop();
        }
        return true;
    }

    @Override
    public void send(Msg msg) {
        outputMessages.add(msg);
    }

    @Override
    public Msg poll() {
        return inputMessages.poll();
    }

    @Override
    public Msg take() throws InterruptedException {
        return inputMessages.take();
    }

    @Override
    public void close() throws IOException {
        executor.shutdownNow();
        if (!socket.isClosed())
            socket.close();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public ChannelType getType() {
        return type;
    }
}
