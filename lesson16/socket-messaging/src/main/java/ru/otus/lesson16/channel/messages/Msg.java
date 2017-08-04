package ru.otus.lesson16.channel.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by piphonom
 */
public abstract class Msg implements Serializable {
    private long channelId;
    private long requestorId;

    /* Effective Java. Item 74 */
    private enum State {NEW, INITIALIZING, INITIALIZED};

    private final AtomicReference<State> initState = new AtomicReference<>(State.NEW);

    Msg() {
        channelId = -1;
        requestorId = -1;
    }

    public Msg(long channelId) {
        init(channelId);
    }

    final void init(long channelId) {
        if (!initState.compareAndSet(State.NEW, State.INITIALIZING))
            throw new IllegalStateException("Already initialized");
        this.channelId = channelId;
        initState.set(State.INITIALIZED);
    }

    private void checkInitialized() {
        if (initState.get() != State.INITIALIZED)
            throw new IllegalStateException("Uninitialized message");
    }

    public long getChannelId() {
        checkInitialized();
        return channelId;
    }

    public void setChannelId(long channelId) {
        checkInitialized();
        this.channelId = channelId;
    }

    public long getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(long requestorId) {
        this.requestorId = requestorId;
    }

    void writeObject(ObjectOutputStream outStream) throws IOException {
        outStream.defaultWriteObject();
        outStream.writeLong(getChannelId());
    }

    void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        inStream.defaultReadObject();
        long inId = inStream.readLong();
        init(inId);
    }
}
