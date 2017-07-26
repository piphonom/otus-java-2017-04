package ru.otus.lesson15.service.messaging.database;

import ru.otus.lesson15.base.messaging.DBMessageService;
import ru.otus.lesson15.base.messaging.MessageSystemContext;
import ru.otus.lesson15.base.orm.DBService;
import ru.otus.lesson15.base.datasets.UserDataSet;
import ru.otus.lesson15.messagesystem.Address;
import ru.otus.lesson15.messagesystem.Addressee;

/**
 * Created by piphonom
 */
public class DBMessageServiceImpl implements DBMessageService, Addressee {

    MessageSystemContext context;
    Address address;
    DBService dbService;

    public DBMessageServiceImpl(MessageSystemContext context, Address address, DBService dbService) {
        this.context = context;
        this.address = address;
        this.dbService = dbService;
    }

    @Override
    public void init() {
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public UserDataSet getUserByName(String name) {
        return dbService.readByName(name);
    }
}
