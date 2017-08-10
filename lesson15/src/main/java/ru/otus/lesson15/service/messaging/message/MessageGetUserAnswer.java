package ru.otus.lesson15.service.messaging.message;

import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.messagesystem.Address;

/**
 * Created by piphonom
 */
public class MessageGetUserAnswer extends MessageToFrontend {

    UserDataSet user;
    int requestorId;

    public MessageGetUserAnswer(Address from, Address to, UserDataSet user, int requestorId) {
        super(from, to);
        this.user = user;
        this.requestorId = requestorId;
    }

    @Override
    public void exec(FrontendMessageService frontendService) {
        frontendService.sendUser(user, requestorId);
    }
}
