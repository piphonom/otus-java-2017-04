package ru.otus.lesson16.channel;

import ru.otus.lesson16.channel.messages.Msg;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.Future;

/**
 * Created by tully.
 */
public interface MsgChannel {

    Queue<Future<Boolean>> init();

    void send(Msg msg);

    Msg poll();

    Msg take() throws InterruptedException;

    void close() throws IOException;

    long getId();

    ChannelType getType();
}
