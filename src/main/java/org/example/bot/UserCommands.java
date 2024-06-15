package org.example.bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import static org.example.DB.Admin.PhotoRepository.getPhotoIdByCaption;
import static org.example.DB.Admin.UserRepository.getUserRole;
import static org.example.DB.User.Register.*;
import static org.example.DB.User.SavaReport.*;

public class UserCommands {
    private final TelegramBot bot;
    private final KeyboardBuilder keyboardBuilder = new KeyboardBuilder();

    public UserCommands(TelegramBot bot) {
        this.bot = bot;
    }
    public void RegisterCommand(Message message, SendMessage sendMessage){
        sendMessage.setReplyMarkup(keyboardBuilder.getMainUserKeyboard());
        long telegramUserId = message.getFrom().getId();
        String username = message.getFrom().getFirstName();
        if (isUserRegistered(telegramUserId)) {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Ошибка. Вы уже зарегистрированы");

        } else {
            registerUser(telegramUserId, username);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Вы успешно зарегистрированы!");
        }
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void UnregisterCommand(Message message, SendMessage sendMessage){
        sendMessage.setReplyMarkup(keyboardBuilder.getMainNotUserKeyboard());
        long telegramUserId = message.getFrom().getId();
        if (isUserRegistered(telegramUserId)) {
            unregisterUser(telegramUserId);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Вы успешно УДАЛЕНЫ ИЗ ЭТОГО МИРА!");
        }else{
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Ошибка. Вы не зарегистрированы");
        }
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void ReplyToUser(Message message, SendMessage sendMessage){
        String telegramName = message.getFrom().getFirstName();
        String response = message.getText();
        String caption = message.getReplyToMessage().getCaption();

        int photoId = getPhotoIdByCaption(caption);
        if(photoId!=-1) {
            if(response.length()==1 && response.matches("[1-5]")){
                int rating = Integer.parseInt(response);
                savePhotoRating(photoId,rating);
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Оценка: '" + rating + "'\nУспешно сохранена");
            }else if (response.endsWith("?")){
                saveUserQuestion(telegramName, response, photoId);
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Вопрос: '" + response + "'\nУспешно сохранён");
            }else{
                saveUserResponse(telegramName, response, photoId);
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Ответ: '" + response + "'\nУспешно сохранён");
            }
        }else{
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Ошибка: не удалось найти фото с указанной подписью.");
        }
        try{
            bot.execute(sendMessage);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }

    }

    public void WelcomeMessage(Message message,SendMessage sendMessage){
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("Добро пожаловать!");
        long telegramUserId = message.getFrom().getId();
        if (isUserRegistered(telegramUserId)) {
            if ("user".equals(getUserRole(telegramUserId))) {
                sendMessage.setReplyMarkup(keyboardBuilder.getMainUserKeyboard());
            } else {
                sendMessage.setReplyMarkup(keyboardBuilder.getMainAdminKeyboard());
            }

        }else{
            sendMessage.setReplyMarkup(keyboardBuilder.getMainNotUserKeyboard());
        }
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
