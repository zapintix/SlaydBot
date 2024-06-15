package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramBot extends TelegramLongPollingBot {

    private AdminReportHandler adminReportHandler;
    private UserCommands userCommands;
    public TelegramBot() {
        adminReportHandler = new AdminReportHandler(this);
        userCommands = new UserCommands(this);
    }



    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage() && update.getMessage().isReply()) {
            userCommands.ReplyToUser(message,sendMessage);

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String command = message.getText();
            if("/most_ratings".equals(command)){
                adminReportHandler.handleMostRatingsCommand(message, sendMessage);

            }else if("/most_question".equals(command)){
                adminReportHandler.handleMostQuestionCommand(message, sendMessage);

            }else if("/most_response".equals(command)){
                adminReportHandler.handleMostResponseCommand(message, sendMessage);

            }else if("/height_ratings".equals(command)) {
                adminReportHandler.handleHighestRatingsCommand(message, sendMessage);

            }else if ("/slays".equals(command)) {
                adminReportHandler.SlaysReportCommand(message, sendMessage);

            }else if ("/register".equals(command)) {
                userCommands.RegisterCommand(message,sendMessage);

            }else if("/unregister".equals(command)){
                userCommands.UnregisterCommand(message,sendMessage);

            } else if ("/start".equals(command)) {
                userCommands.WelcomeMessage(message, sendMessage);
            }

        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            adminReportHandler.SendPhotoToUsers(message, update);

        }else if(update.hasCallbackQuery()){
            adminReportHandler.handleCallbackQuery(update.getCallbackQuery());
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
