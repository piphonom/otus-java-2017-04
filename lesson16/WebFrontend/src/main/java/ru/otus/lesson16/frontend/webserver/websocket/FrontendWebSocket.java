package ru.otus.lesson16.frontend.webserver.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.channel.messages.GetUserMsg;
import ru.otus.lesson16.channel.messages.Msg;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessor;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessorsRegistry;

import java.io.IOException;

@WebSocket
public class FrontendWebSocket implements DbAnswerProcessor {

    private DbAnswerProcessorsRegistry answerProcessorsRegistry;
    private long requestorId;
    private Session session;
    private MsgChannel channel;
    private Gson gson = new Gson();

    public FrontendWebSocket(MsgChannel channel, DbAnswerProcessorsRegistry registry, long id) {
        this.channel = channel;
        this.answerProcessorsRegistry = registry;
        this.requestorId = id;
        registry.register(this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        GetUserMsg getUser = new GetUserMsg(channel.getId(), data);
        getUser.setRequestorId(requestorId);
        channel.send(getUser);
    }

    @Override
    public void onAnswer(Msg answer) {
        String message = gson.toJson(answer);
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getId() {
        return requestorId;
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        setSession(session);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        answerProcessorsRegistry.unregister(this);
    }
}
