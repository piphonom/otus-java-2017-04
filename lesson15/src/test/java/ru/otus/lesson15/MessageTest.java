package ru.otus.lesson15;

import org.junit.Test;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.base.messaging.DBMessageService;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.messaging.MessageSystemContext;
import ru.otus.lesson15.base.orm.DBService;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.MessageSystem;
import ru.otus.lesson15.service.messaging.database.DBMessageServiceImpl;
import ru.otus.lesson15.service.messaging.frontend.FrontendMessageServiceImpl;
import ru.otus.lesson15.service.orm.DBServiceImpl;
import ru.otus.lesson15.service.orm.connectors.H2SimpleConnector;


/**
 * Created by piphonom
 */
public class MessageTest {
    @Test
    public void FrontendMessageTest() throws Exception {

        MessageSystem messageSystem = new MessageSystem();

        MessageSystemContext context = new MessageSystemContext(messageSystem);
        Address frontAddress = new Address("Frontend");
        context.setFrontAddress(frontAddress);
        Address dbAddress = new Address("DB");
        context.setDbAddress(dbAddress);

        FrontendMessageService frontendMessageService = new FrontendMessageServiceImpl(context, frontAddress);
        frontendMessageService.init();

        String userName = "Michael Jackson";
        UserDataSet user = new UserDataSet();
        user.setName(userName);
        user.setAge(56);
        DBService dbService = new DBServiceImpl(H2SimpleConnector::new);
        dbService.save(user);

        DBMessageService dbMessageService = new DBMessageServiceImpl(context, dbAddress, dbService);
        dbMessageService.init();

        messageSystem.start();

        frontendMessageService.handleRequest(userName, this);

        Thread.sleep(10000);
    }
}
