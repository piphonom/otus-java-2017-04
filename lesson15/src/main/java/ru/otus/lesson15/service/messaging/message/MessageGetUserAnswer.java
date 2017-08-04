package ru.otus.lesson15.service.messaging.message;

import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.messagesystem.Address;

/**
 * Created by piphonom
 */
public class MessageGetUserAnswer extends MessageToFrontend {

    UserDataSet user;
    Object requestor;

    public MessageGetUserAnswer(Address from, Address to, UserDataSet user, Object requestor) {
        super(from, to);
        this.user = user;
        this.requestor = requestor;
    }

    @Override
    public void exec(FrontendMessageService frontendService) {
        frontendService.sendUser(user, requestor);
    }
}
