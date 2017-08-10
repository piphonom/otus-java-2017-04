package ru.otus.lesson15.service.messaging.message;

import ru.otus.lesson15.base.messaging.DBMessageService;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Message;
import ru.otus.lesson15.messagesystem.MessageSystem;


/**
 * Created piphonom
 */
public class MessageGetUser extends MessageToDB {

    private final MessageSystem messageSystem;
    private final String userName;
    int requestorId;

    public MessageGetUser(Address from, Address to, MessageSystem messageSystem, String userName, int requestorId) {
        super(from, to);
        this.messageSystem = messageSystem;
        this.userName = userName;
        this.requestorId = requestorId;
    }

    @Override
    public void exec(DBMessageService dbMessageService) {
        UserDataSet user = dbMessageService.getUserByName(userName);
        Message answer = new MessageGetUserAnswer(getTo(), getFrom(), user, requestorId);
        messageSystem.sendMessage(answer);
    }
}
