package ru.otus.lesson16.channel.socket;

import ru.otus.lesson16.channel.ChannelRegistry;
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
public class ServerSocketMsgChannel extends SocketMsgChannel {

    ChannelRegistry registry;

    public ServerSocketMsgChannel(Socket socket, long channelId, SocketChannelRegistry registry) throws IOException {
        super(socket, channelId);
        this.registry = registry;
    }

    /* Reverse order init of Server's and Client's objects streams */
    @Override
    void initStreams() throws IOException {
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    boolean handShake() {
        Msg msg;
        if (socket.isConnected()) {
            try {
                msg = new ChannelInitIdMsg(id);
                outputStream.writeObject(msg);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Handshake failed: " + e.getMessage());
                return false;
            }
        } else {
            return false;
        }
        if (socket.isConnected()) {
            try {
                msg = (Msg)inputStream.readObject();
                if (msg == null || !(msg instanceof ChannelInitTypeMsg)) {
                    logger.log(Level.SEVERE, "Handshake failed: bad channel type info");
                    return false;
                }
                if (msg.getChannelId() != this.id) {
                    logger.log(Level.SEVERE, "Handshake failed: wrong channel id");
                    return false;
                }
                this.type = ((ChannelInitTypeMsg) msg).getType();
                registry.register(this);
                return true;
            } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Handshake failed: " + e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void safeStop() {
        registry.unregister(this);
    }
}
