package ru.otus.lesson16.channel.socket;

import ru.otus.lesson16.channel.ChannelType;

import java.io.IOException;

/**
 * Created by piphonom
 */
public class ClientSocketMsgChannelProducer extends ClientSocketMsgChannel {
    public ClientSocketMsgChannelProducer(String host, int port) throws IOException {
        super(host, port, ChannelType.PRODUCER);
    }
}
