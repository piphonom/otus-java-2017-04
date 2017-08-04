package ru.otus.lesson15.webserver.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import ru.otus.lesson15.base.messaging.FrontendMessageService;

import java.io.IOException;

@WebSocket
public class FrontendWebSocket {
    private Session session;
    FrontendMessageService frontendMessageService;

    public FrontendWebSocket(FrontendMessageService frontendMessageService) {
        this.frontendMessageService = frontendMessageService;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        frontendMessageService.handleRequest(data, this);
    }

    public void sendMessage(String message) {
        try {
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    }
}
