package org.example.Mediator;

import org.example.bot.AdminReportHandler;
import org.example.bot.KeyboardBuilder;
import org.example.bot.TelegramBot;
import org.example.bot.UserCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBotMediator implements Mediator {
    private final TelegramBot bot;
    private final AdminReportHandler adminReportHandler;
    private final UserCommands userCommands;
    private final KeyboardBuilder keyboardBuilder;

    public TelegramBotMediator(TelegramBot bot, AdminReportHandler adminReportHandler, UserCommands userCommands, KeyboardBuilder keyboardBuilder) {
        this.bot = bot;
        this.adminReportHandler = adminReportHandler;
        this.userCommands = userCommands;
        this.keyboardBuilder = keyboardBuilder;
    }


    @Override
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        adminReportHandler.handleCallbackQuery(callbackQuery);
    }

    @Override
    public void sendPhotoToUsers(Message message, Update update) {
        adminReportHandler.SendPhotoToUsers(message, update);
    }


    @Override
    public void registerCommand(Message message, SendMessage sendMessage) {
        userCommands.RegisterCommand(message, sendMessage);
    }

    @Override
    public void unregisterCommand(Message message, SendMessage sendMessage) {
        userCommands.UnregisterCommand(message, sendMessage);
    }

    @Override
    public void replyToUser(Message message, SendMessage sendMessage) {
        userCommands.ReplyToUser(message, sendMessage);
    }

    @Override
    public void welcomeMessage(Message message, SendMessage sendMessage) {
        userCommands.WelcomeMessage(message, sendMessage);
    }
    @Override
    public void handleMostRatingsCommand(Message message, SendMessage sendMessage) {
        adminReportHandler.handleMostRatingsCommand(message, sendMessage);
    }
    @Override
    public  void handleMostQuestionCommand(Message message, SendMessage sendMessage) {
        adminReportHandler.handleMostQuestionCommand(message, sendMessage);
    }
    @Override
    public void handleMostResponseCommand(Message message, SendMessage sendMessage){
        adminReportHandler.handleMostResponseCommand(message, sendMessage);
    }
    @Override
    public void handleHighestRatingsCommand(Message message, SendMessage sendMessage){
        adminReportHandler.handleHighestRatingsCommand(message, sendMessage);
    }
    @Override
    public void slaysReportCommand(Message message, SendMessage sendMessage){
        adminReportHandler.SlaysReportCommand(message, sendMessage);
    }
}