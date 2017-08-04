package ru.otus.lesson16.channel.messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by piphonom
 */
public class GetUserAnswerMsg extends Msg {
    private String name;
    private int age = 0;
    private List<String> phones;

    public GetUserAnswerMsg(long channelId, String name, int age, List<String> phones) {
        super(channelId);
        this.name = name;
        this.age = age;
        this.phones = phones;
    }

    void writeObject(ObjectOutputStream outStream) throws IOException {
        super.writeObject(outStream);
        if (name != null) {
            outStream.writeObject(name);
        } else {
            outStream.writeObject("");
        }
        outStream.writeInt(age);
        if (phones != null) {
            outStream.writeInt(phones.size());
            for (String phone : phones) {
                outStream.writeObject(phone);
            }
        } else {
            outStream.writeInt(0);
        }
    }

    void readObject(ObjectInputStream inStream)  throws IOException, ClassNotFoundException {
        super.readObject(inStream);
        Object readObject = inStream.readObject();
        if ((readObject != null) && (readObject instanceof String)) {
            name = (String) readObject;
        }
        age = inStream.readInt();
        int phonesSize =  inStream.readInt();
        if (phonesSize > 0) {
            phones = new ArrayList<>(phonesSize);
            while (phonesSize-- > 0) {
                readObject = inStream.readObject();
                if ((readObject != null) && (readObject instanceof String)) {
                    phones.add((String)  readObject);
                }
            }
        }
    }
}
