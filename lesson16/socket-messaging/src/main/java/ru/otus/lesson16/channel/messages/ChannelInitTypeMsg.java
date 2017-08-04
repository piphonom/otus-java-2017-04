package ru.otus.lesson16.channel.messages;

import ru.otus.lesson16.channel.ChannelType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by piphonom
 */
public class ChannelInitTypeMsg extends Msg {
    private ChannelType type;

    public ChannelInitTypeMsg(long channelId, ChannelType type) {
        super(channelId);
        this.type = type;
    }

    public ChannelType getType() {
        return type;
    }

    void writeObject(ObjectOutputStream outStream) throws IOException {
        super.writeObject(outStream);
        outStream.writeObject(type);
    }

    void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        super.readObject(inStream);
        Object readType = inStream.readObject();
        if (readType == null || !(readType instanceof ChannelType)) {
            throw new IOException("Bad deserialization");
        }
        type = (ChannelType) readType;
    }

    private static final long serialVersionUID = 241578435860954L;
}
