package ru.otus.lesson16.channel.socket;

import ru.otus.lesson16.channel.ChannelRegistry;
import ru.otus.lesson16.channel.MsgChannel;

import java.util.Map;

/**
 * Created by piphonom
 */
public interface SocketChannelRegistry extends ChannelRegistry {
    /**
     * Used to get the map of registered consumers
     * @return Synchronized map of client's channels that will process requests
     */
    Map<Long, MsgChannel> getConsumers();

    /**
     * Used to get the map of registered producers
     * @return Synchronized map of client's channels that will process requests
     */
    Map<Long, MsgChannel> getProducers();
}
