package ru.otus.lesson15.webserver.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.lesson15.base.messaging.FrontendMessageService;


public class FrontendWebSocketCreator implements WebSocketCreator {

    FrontendMessageService frontendMessageService;

    public FrontendWebSocketCreator(FrontendMessageService frontendMessageService) {
        this.frontendMessageService = frontendMessageService;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        FrontendWebSocket socket = new FrontendWebSocket(frontendMessageService);
        return socket;
    }
}
