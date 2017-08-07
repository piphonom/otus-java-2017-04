package ru.otus.lesson16.channel.socket;

import ru.otus.lesson16.channel.MsgChannel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by piphonom
 */
public class SocketChannelRegistryImpl implements SocketChannelRegistry {

    /*
    * TODO: try with ConcurrentHashMap
    * */
    private Map<Long, MsgChannel> consumers = Collections.synchronizedMap(new HashMap<>());
    private Map<Long, MsgChannel> producers = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void register(MsgChannel channel) {
        switch (channel.getType()) {
            case PRODUCER:
                producers.put(channel.getId(), channel);
                break;
            case CONSUMER:
                consumers.put(channel.getId(), channel);
                break;
        }
    }

    @Override
    public void unregister(MsgChannel channel) {
        switch (channel.getType()) {
            case PRODUCER:
                producers.remove(channel.getId());
                break;
            case CONSUMER:
                consumers.remove(channel.getId());
                break;
        }
    }

    @Override
    public void unregisterAll() {
        producers.clear();
        consumers.clear();
    }

    @Override
    public Map<Long, MsgChannel> getConsumers() {
        return consumers;
    }

    @Override
    public Map<Long, MsgChannel> getProducers() {
        return producers;
    }
}
