package ru.otus.lesson15.service.messaging.message;

import ru.otus.lesson15.base.messaging.DBMessageService;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Addressee;
import ru.otus.lesson15.messagesystem.Message;


public abstract class MessageToDB extends Message {
    public MessageToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        if (addressee instanceof DBMessageService) {
            exec((DBMessageService) addressee);
        }
    }

    public abstract void exec(DBMessageService dbMessageService);
}
