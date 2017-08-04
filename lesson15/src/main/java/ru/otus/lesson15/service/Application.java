package ru.otus.lesson15.service;

import ru.otus.lesson15.base.messaging.DBMessageService;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.messaging.MessageSystemContext;
import ru.otus.lesson15.base.orm.DBService;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.MessageSystem;
import ru.otus.lesson15.service.messaging.database.DBMessageServiceImpl;
import ru.otus.lesson15.service.messaging.frontend.FrontendMessageServiceImpl;
import ru.otus.lesson15.service.orm.DBServiceImpl;
import ru.otus.lesson15.service.orm.connectors.MySqlSimpleConnector;
import ru.otus.lesson15.webserver.WebServer;

/**
 * Created by piphonom
 */
public class Application implements Runnable {

    DBService dbService = new DBServiceImpl(MySqlSimpleConnector::new);

    @Override
    public void run() {
        FrontendMessageService frontendMessageService = runBackend(dbService);
        WebServer webServer = runFrontend(dbService, frontendMessageService);
        try {
            webServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FrontendMessageService runBackend(DBService dbService) {
        MessageSystem messageSystem = new MessageSystem();

        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);

        FrontendMessageService frontendMessageService = new FrontendMessageServiceImpl(context, frontAddress);
        frontendMessageService.init();

        DBMessageService dbMessageService = new DBMessageServiceImpl(context, dbAddress, dbService);
        dbMessageService.init();

        messageSystem.start();

        return frontendMessageService;
    }

    private WebServer runFrontend(DBService dbService, FrontendMessageService frontendMessageService) {
        WebServer webServer = null;
        try {
            webServer =  new WebServer.Builder()
                    .setLogin("admin")
                    .setPassword("nimda")
                    .setPort(8080)
                    .setHandledObject(dbService.getCacheEngine())
                    .setFrontendMessageService(frontendMessageService)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webServer;
    }
}
