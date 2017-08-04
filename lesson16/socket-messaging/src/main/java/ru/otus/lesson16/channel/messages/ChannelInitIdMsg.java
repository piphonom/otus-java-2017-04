package ru.otus.lesson16.channel.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by piphonom
 */
public final class ChannelInitIdMsg extends Msg {

    public ChannelInitIdMsg(long channelId) {
        super(channelId);
    }

    void writeObject(ObjectOutputStream outStream) throws IOException {
        super.writeObject(outStream);
    }

    void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        super.readObject(inStream);
    }

    private static final long serialVersionUID = 885183585860954L;
}
