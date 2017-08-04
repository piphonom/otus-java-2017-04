package ru.otus.lesson16.channel.socket;

import ru.otus.lesson16.channel.ChannelType;
import ru.otus.lesson16.channel.messages.ChannelInitIdMsg;
import ru.otus.lesson16.channel.messages.ChannelInitTypeMsg;
import ru.otus.lesson16.channel.messages.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;

/**
 * Created by piphonom
 */
public abstract class ClientSocketMsgChannel extends SocketMsgChannel {

    public ClientSocketMsgChannel(String host, int port, ChannelType type) throws IOException {
        super(new Socket(host, port), -1);
        this.type = type;
    }

    /* Reverse order init of Server's and Client's objects streams */
    @Override
    void initStreams() throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    boolean handShake() {
        Msg msg;
        if (socket.isConnected()) {
            try {
                msg = (Msg) inputStream.readObject();
                if ((msg == null) || !(msg instanceof ChannelInitIdMsg)) {
                    logger.log(Level.SEVERE, "Handshake failed: bad channel id");
                    return false;
                }
                this.id = msg.getChannelId();
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Handshake failed: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        if (socket.isConnected()) {
            try {
                msg = new ChannelInitTypeMsg(this.id, type);
                outputStream.writeObject(msg);
                return true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Handshake failed: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void safeStop() {
        super.safeStop();
    }
}
