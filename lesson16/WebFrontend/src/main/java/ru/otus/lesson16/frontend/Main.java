package ru.otus.lesson16.frontend;

import org.apache.commons.cli.*;
import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.channel.messages.Msg;
import ru.otus.lesson16.channel.socket.ClientSocketMsgChannelProducer;
import ru.otus.lesson16.frontend.webserver.DbAnswerProcessor;
import ru.otus.lesson16.frontend.webserver.WebServer;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created piphonom
 */
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 5050;
    private static final String DEFAULT_LOGIN = "admin";
    private static final String DEFAULT_PASSWORD = "nimda";
    private static final int DEFAULT_PORT = 8080;

    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static String login;
    private static String password;
    private static int port;

    public static void main(String[] args) throws Exception {
        commandLineParser(args);
        new WebService().execute();
    }

    private static void commandLineParser(String[] args) {
        Options options = new Options();

        Option loginOption = new Option("l", "login", true, "login");
        loginOption.setRequired(false);
        options.addOption(loginOption);

        Option passwordOption = new Option("s", "password", true, "password");
        passwordOption.setRequired(false);
        options.addOption(passwordOption);

        Option portOption = new Option("p", "port", true, "port");
        portOption.setRequired(false);
        options.addOption(portOption);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Command line parsing error");
            return;
        }

        login = (cmd.getOptionValue("login") != null) ? cmd.getOptionValue("login") : DEFAULT_LOGIN;
        password = (cmd.getOptionValue("password") != null) ? cmd.getOptionValue("password") : DEFAULT_PASSWORD;
        port = (cmd.getOptionValue("port") != null) ? Integer.valueOf(cmd.getOptionValue("port")) : DEFAULT_PORT;
    }

    private static final class WebService {
        private WebServer webServer;
        private ExecutorService executor = Executors.newSingleThreadExecutor();
        private MsgChannel channel;

        public WebService() throws Exception {
            channel = new ClientSocketMsgChannelProducer(HOST, PORT);
            this.webServer = new WebServer.Builder()
                    .setLogin(login)
                    .setPassword(password)
                    .setPort(port)
                    .setMsgChannel(channel)
                    .build();
        }

        public void execute() throws Exception {
            Queue<Future<Boolean>> socketTasks = channel.init();
            try {
                webServer.start();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to start WEB server");
                try {
                    channel.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
            executor.execute(this::serviceTask);
            Future<Boolean> taskFinished;
            while ((taskFinished = socketTasks.poll()) != null) {
                try {
                    taskFinished.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            executor.shutdownNow();
            webServer.stop();
        }

        private void serviceTask() {
            while (!executor.isShutdown()) {
                try
                {
                    Msg msg = channel.take();
                    if (msg != null) {
                        DbAnswerProcessor processor = webServer.getAnswerProcessorRegistry().getByRequestorId(msg.getRequestorId());
                        if (processor != null) {
                            processor.onAnswer(msg);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
