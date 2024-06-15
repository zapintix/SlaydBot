package org.example.Mediator;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Mediator {
    void handleCallbackQuery(CallbackQuery callbackQuery);
    void sendPhotoToUsers(Message message, Update update);
    void registerCommand(Message message, SendMessage sendMessage);
    void unregisterCommand(Message message, SendMessage sendMessage);
    void replyToUser(Message message, SendMessage sendMessage);
    void welcomeMessage(Message message, SendMessage sendMessage);
    void handleMostRatingsCommand(Message message, SendMessage sendMessage);
    void handleMostQuestionCommand(Message message, SendMessage sendMessage);
    void handleMostResponseCommand(Message message, SendMessage sendMessage);
    void handleHighestRatingsCommand(Message message, SendMessage sendMessage);
    void slaysReportCommand(Message message, SendMessage sendMessage);
}
