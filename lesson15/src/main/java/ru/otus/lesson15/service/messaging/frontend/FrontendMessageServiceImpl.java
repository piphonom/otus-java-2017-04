package ru.otus.lesson15.service.messaging.frontend;

import com.google.gson.Gson;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.messaging.MessageSystemContext;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.service.messaging.message.MessageGetUser;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Addressee;
import ru.otus.lesson15.messagesystem.Message;
import ru.otus.lesson15.webserver.websocket.FrontendWebSocket;

/**
 * Created by piphonom
 */
public class FrontendMessageServiceImpl implements FrontendMessageService, Addressee {

    MessageSystemContext context;
    Address address;

    public FrontendMessageServiceImpl(MessageSystemContext context, Address address) {
        this.context = context;
        this.address = address;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public void handleRequest(String username, Object requestor) {
        Message message = new MessageGetUser(getAddress(), context.getDbAddress(), context.getMessageSystem(), username, requestor);
        context.getMessageSystem().sendMessage(message);
    }

    @Override
    public void sendUser(UserDataSet user, Object requestor) {
        if (requestor instanceof FrontendWebSocket) {
            ((FrontendWebSocket)requestor).sendMessage(new Gson().toJson(user));
        }
    }
}
