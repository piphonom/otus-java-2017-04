package ru.otus.lesson15.base.messaging;

import ru.otus.lesson15.base.datasets.UserDataSet;

/**
 * Created by tully.
 */
public interface FrontendMessageService {
    void init();

    void handleRequest(String name, Object requestor);

    void sendUser(UserDataSet user, int requestorId);
}
