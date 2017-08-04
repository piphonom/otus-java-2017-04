package ru.otus.lesson16.frontend.webserver.servlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessorsRegistry;
import ru.otus.lesson16.frontend.webserver.websocket.FrontendWebSocketCreator;

public class DBServiceServlet extends WebSocketServlet {
    private final static int LOGOUT_TIME = 10 * 60 * 1000;
    private MsgChannel channel;
    private FrontendWebSocketCreator creator;

    public DBServiceServlet(MsgChannel channel) {
        this.channel = channel;
        this.creator = new FrontendWebSocketCreator(channel);
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator(creator);
    }

    public DbAnswerProcessorsRegistry getAnswerProcessorRegistry() {
        return creator;
    }
}
