package ru.otus.lesson16.frontend.webserver;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.frontend.webserver.servlet.DBServiceServlet;
import ru.otus.lesson16.frontend.webserver.servlet.LoginServlet;

/**
 * Created by piphonom
 */
public class WebServer {

    /**
     * TODO:  fix  STATIC_HTML_SOURCE
    * */
    private final static String STATIC_HTML_SOURCE = "html";

    private final Server server;

    private final DBServiceServlet dbServiceServlet;

    private WebServer(int port, String login, String password, MsgChannel channel) throws Exception {

        ResourceHandler resourceHandler = new ResourceHandler();

        resourceHandler.setBaseResource(Resource.newClassPathResource(STATIC_HTML_SOURCE));

        ServletContextHandler context = new ServletContextHandler(
                ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        dbServiceServlet = new DBServiceServlet(channel);
        context.addServlet(new ServletHolder(new LoginServlet(login, password)), "/login");
        context.addServlet(new ServletHolder(dbServiceServlet), "/dbservice");

        server = new Server(port);
        server.setHandler(new HandlerList(resourceHandler, context));
    }

    public void start() throws Exception {
        server.start();
        //server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }

    public DbAnswerProcessorsRegistry getAnswerProcessorRegistry() {
        return dbServiceServlet.getAnswerProcessorRegistry();
    }

    public static class Builder {
        private int port;
        private String login;
        private String password;
        private MsgChannel channel;

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

        public Builder setMsgChannel(MsgChannel channel) {
            this.channel = channel;
            return this;
        }

        public WebServer build() throws Exception {
            return new WebServer(port, login, password, channel);
        }
    }
}
