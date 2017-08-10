package ru.otus.lesson15.service.messaging.frontend;

import com.google.gson.Gson;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.messaging.MessageSystemContext;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Addressee;
import ru.otus.lesson15.messagesystem.Message;
import ru.otus.lesson15.service.messaging.message.MessageGetUser;
import ru.otus.lesson15.webserver.websocket.FrontendWebSocket;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by piphonom
 */
public class FrontendMessageServiceImpl implements FrontendMessageService, Addressee {

    MessageSystemContext context;
    Address address;
    Map<Integer, Object> requestors = new HashMap<>();
    int lastRequestorId = 0;

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
        requestors.put(lastRequestorId, requestor);
        Message message = new MessageGetUser(getAddress(), context.getDbAddress(), context.getMessageSystem(), username, lastRequestorId);
        context.getMessageSystem().sendMessage(message);
        lastRequestorId++;
    }

    @Override
    public void sendUser(UserDataSet user, int requestorId) {
        Object requestor = requestors.remove(requestorId);
        if (requestor != null && requestor instanceof FrontendWebSocket) {
            ((FrontendWebSocket)requestor).sendMessage(new Gson().toJson(user));
        }
    }
}
