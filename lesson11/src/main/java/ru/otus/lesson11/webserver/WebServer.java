package ru.otus.lesson11.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by piphonom
 */
public class WebServer {

    private final Server server;

    private WebServer(int port, String login, String password, Object handledObject) throws Exception {
        server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        context.addServlet(RootServlet.class, "/");
        context.addServlet(new ServletHolder(new SettingsServlet(handledObject, login, password)), "/settings");
        server.setHandler(context);
    }

    public void start() throws Exception {
        server.start();
        //server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public static class Builder {
        private int port;
        private String login;
        private String password;
        private Object handledObject;

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

        public WebServer build() throws Exception {
            return new WebServer(port, login, password, handledObject);
        }
    }
}
