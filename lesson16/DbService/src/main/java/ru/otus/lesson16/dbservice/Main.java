package ru.otus.lesson16.dbservice;

import ru.otus.lesson16.channel.MsgChannel;
import ru.otus.lesson16.channel.messages.GetUserAnswerMsg;
import ru.otus.lesson16.channel.messages.GetUserMsg;
import ru.otus.lesson16.channel.messages.Msg;
import ru.otus.lesson16.channel.socket.ClientSocketMsgChannelConsumer;
import ru.otus.lesson16.dbservice.datasets.UserDataSet;
import ru.otus.lesson16.dbservice.implementation.DBServiceHibernateImpl;
import ru.otus.lesson16.dbservice.orm.DBService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created piphonom
 */
public class Main {
    private static final String HOST = "localhost";
    private static final int PORT = 5050;

    public static void main(String[] args) {
        new MessagingService().execute();
    }

    private static final class MessagingService {

        private DBService dbService;
        private ExecutorService executor = Executors.newSingleThreadExecutor();
        private MsgChannel channel;

        MessagingService() {
            dbService = new DBServiceHibernateImpl();//new DBServiceImpl(MySqlSimpleConnector::new);
            try {
                channel = new ClientSocketMsgChannelConsumer(HOST, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void execute() {
            Queue<Future<Boolean>> socketTasks = channel.init();
            executor.execute(this::serviceTask);
            Future<Boolean> taskFinished;
            while ((taskFinished = socketTasks.poll()) != null) {
                try {
                    taskFinished.get();
                } catch (InterruptedException | ExecutionException  e) {
                    e.printStackTrace();
                }
            }
            executor.shutdownNow();
            dbService.shutdown();
        }

        private void serviceTask() {
            while (!executor.isShutdown()) {
                try
                {
                    Msg msg = channel.take();
                    if (msg != null) {
                        if (msg instanceof GetUserMsg) {
                            UserDataSet user = dbService.readByName(((GetUserMsg) msg).getUserName());
                            if (user != null) {
                                List<String> phones = new ArrayList<>(user.getPhones().size());
                                user.getPhones().forEach(phoneDataSet -> phones.add(phoneDataSet.getNumber()));
                                GetUserAnswerMsg answer = new GetUserAnswerMsg(msg.getChannelId(), user.getName(), user.getAge(), phones);
                                answer.setRequestorId(msg.getRequestorId());
                                channel.send(answer);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
