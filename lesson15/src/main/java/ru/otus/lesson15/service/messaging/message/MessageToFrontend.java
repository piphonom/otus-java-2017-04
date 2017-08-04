package ru.otus.lesson15.service.messaging.message;

import ru.otus.lesson15.base.messaging.FrontendMessageService;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Addressee;
import ru.otus.lesson15.messagesystem.Message;

public abstract class MessageToFrontend extends Message {
    public MessageToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof FrontendMessageService) {
            exec((FrontendMessageService) addressee);
        }
    }

    public abstract void exec(FrontendMessageService frontendService);
}