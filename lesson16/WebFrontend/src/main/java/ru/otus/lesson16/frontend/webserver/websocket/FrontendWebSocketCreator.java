package ru.otus.lesson16.frontend.webserver.websocket;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessor;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessorsRegistry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FrontendWebSocketCreator implements WebSocketCreator, DbAnswerProcessorsRegistry {

    private MsgChannel channel;
    private long answerProcessorCounter;
    Map<Long, DbAnswerProcessor> answerProcessors = new ConcurrentHashMap<>();

    public FrontendWebSocketCreator(MsgChannel channel) {
        this.channel = channel;
        this.answerProcessorCounter = 0;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        FrontendWebSocket webSocket = new FrontendWebSocket(this.channel, this, answerProcessorCounter++);
        return webSocket;
    }

    @Override
    public void register(DbAnswerProcessor processor) {
        answerProcessors.put(processor.getId(), processor);
    }

    @Override
    public void unregister(DbAnswerProcessor processor) {
        answerProcessors.remove(processor.getId());
    }

    @Override
    public DbAnswerProcessor getByRequestorId(long id) {
        return answerProcessors.get(id);
    }
}
