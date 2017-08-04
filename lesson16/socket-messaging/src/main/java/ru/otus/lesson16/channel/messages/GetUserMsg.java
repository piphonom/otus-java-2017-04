package ru.otus.lesson16.channel.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by piphonom
 */
public final class GetUserMsg extends Msg {
    String userName;

    public GetUserMsg(long channelId, String userName) {
        super(channelId);
        this.userName = userName;
    }

    void writeObject(ObjectOutputStream outStream) throws IOException {
        super.writeObject(outStream);
        outStream.writeObject(userName);
    }

    void readObject(ObjectInputStream inStream) throws IOException, ClassNotFoundException {
        super.readObject(inStream);
        Object name = inStream.readObject();
        if (name == null || !(name instanceof String)) {
            throw new IOException("Bad deserialization");
        }
        userName = (String) name;
    }

    public String getUserName() {
        return userName;
    }

    private static final long serialVersionUID = 685183585860954L;
}
