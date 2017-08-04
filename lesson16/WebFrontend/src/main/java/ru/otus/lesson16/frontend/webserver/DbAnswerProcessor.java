package ru.otus.lesson16.frontend.webserver;

import ru.otus.lesson16.channel.messages.Msg;

/**
 * Created by piphonom
 */
public interface DbAnswerProcessor {
    long getId();
    void onAnswer(Msg answer);
}
