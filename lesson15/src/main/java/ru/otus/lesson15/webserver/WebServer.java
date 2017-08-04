package ru.otus.lesson15.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.webserver.servlet.CacheSettingsServlet;
import ru.otus.lesson15.webserver.servlet.CacheStateServlet;
import ru.otus.lesson15.webserver.servlet.DBServiceServlet;
import ru.otus.lesson15.webserver.servlet.LoginServlet;

/**
 * Created by piphonom
 */
public class WebServer {

    private final static String STATIC_HTML_SOURCE = "html";

    private final Server server;

    private WebServer(int port, String login, String password, Object handledObject, FrontendMessageService messageService) throws Exception {

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(STATIC_HTML_SOURCE);

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder(new LoginServlet(login, password)), "/login");
        context.addServlet(new ServletHolder(new CacheSettingsServlet(handledObject, login, password)), "/settings");
        context.addServlet(new ServletHolder(new CacheStateServlet(handledObject)), "/settingssync");
        context.addServlet(new ServletHolder(new DBServiceServlet(messageService)), "/dbservice");

        server = new Server(port);
        server.setHandler(new HandlerList(resourceHandler, context));
    }

    public void start() throws Exception {
        server.start();
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class Builder {
        private int port;
        private String login;
        private String password;
        private Object handledObject;
        private FrontendMessageService messageService;

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setHandledObject(Object handledObject) {
            this.handledObject = handledObject;
            return this;
        }

        public Builder setFrontendMessageService(FrontendMessageService messageService) {
            this.messageService = messageService;
            return this;
        }

        public WebServer build() throws Exception {
            return new WebServer(port, login, password, handledObject, messageService);
        }
    }
}
