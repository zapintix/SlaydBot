package org.example.bot;

import org.checkerframework.checker.units.qual.K;
import org.example.Mediator.Mediator;
import org.example.Mediator.TelegramBotMediator;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {
    private AdminReportHandler adminReportHandler;
    private Mediator mediator;
    private UserCommands userCommands;
    private KeyboardBuilder keyboardBuilder;
    public TelegramBot() {
        adminReportHandler = new AdminReportHandler(this);
        userCommands = new UserCommands(this);
        keyboardBuilder = new KeyboardBuilder();
        mediator = new TelegramBotMediator (this, adminReportHandler,userCommands, keyboardBuilder);

    }



    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage() && update.getMessage().isReply()) {
            mediator.replyToUser(message, sendMessage);

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String command = message.getText();
            if("/most_ratings".equals(command)){
                mediator.handleMostRatingsCommand(message, sendMessage);

            }else if("/most_question".equals(command)){
                mediator.handleMostQuestionCommand(message, sendMessage);

            }else if("/most_response".equals(command)){
                mediator.handleMostResponseCommand(message, sendMessage);

            }else if("/height_ratings".equals(command)) {
                mediator.handleHighestRatingsCommand(message, sendMessage);

            }else if ("/slays".equals(command)) {
                mediator.slaysReportCommand(message, sendMessage);

            }else if ("/register".equals(command)) {
                mediator.registerCommand(message,sendMessage);

            }else if("/unregister".equals(command)){
                mediator.unregisterCommand(message,sendMessage);

            } else if ("/start".equals(command)) {
                mediator.welcomeMessage(message, sendMessage);
            }

        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            mediator.sendPhotoToUsers(message, update);

        }else if(update.hasCallbackQuery()){
            mediator.handleCallbackQuery(update.getCallbackQuery());
        }
    }

    @Override
    public String getBotUsername() {
        return "VuzSlayd_bot";
    }

    @Override
    public String getBotToken() {
        return "7494948521:AAEgB3t_BOiqJCU_5ikGCHnNa0P0r5hNO3k";
    }
}