package ru.otus.lesson16.channel;

/**
 * Created by piphonom
 */
public interface ChannelRegistry {
    void register(MsgChannel channel);

    void unregister(MsgChannel channel);

    void unregisterAll();
}
