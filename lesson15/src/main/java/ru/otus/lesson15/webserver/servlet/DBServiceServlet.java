package ru.otus.lesson15.webserver.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.webserver.websocket.FrontendWebSocketCreator;

public class DBServiceServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;

    FrontendMessageService frontendMessageService;

    public DBServiceServlet(FrontendMessageService frontendMessageService) {
        this.frontendMessageService = frontendMessageService;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(new FrontendWebSocketCreator(frontendMessageService));
    }
}
