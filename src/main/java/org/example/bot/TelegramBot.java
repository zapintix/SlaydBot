package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;
import static org.example.DB.Admin.PhotoRepository.*;
import static org.example.DB.Admin.ReportRepository.getReport;
import static org.example.DB.Admin.UserRepository.getAllRegisteredUsers;
import static org.example.DB.Admin.UserRepository.getUserRole;
import static org.example.DB.User.Register.*;
import static org.example.DB.User.SavaReport.*;

public class TelegramBot extends TelegramLongPollingBot {

    private final KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
    private ReportHandler reportHandler;

    public void setReportHandler(ReportHandler reportHandler) {
        this.reportHandler = reportHandler; // Метод для установки обработчика отчетов
    }


    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        if (update.hasMessage() && update.getMessage().isReply()) {
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
                execute(sendMessage);
            }catch (TelegramApiException e){
                e.printStackTrace();
            }

        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String command = message.getText();

            if ("/register".equals(command)) {
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
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }else if("/unregister".equals(command)){
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
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if ("/start".equals(command)) {
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
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if ("/slays".equals(command)) {
                long telegramUserId = message.getFrom().getId();
                if ("admin".equals(getUserRole(telegramUserId))) {
                    List<String> captions = getAllSlideIds();
                    if (captions.isEmpty()) {
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Нет доступных слайдов!");
                    } else {
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Выберите слайд для получения отчета:");
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
                        for (String caption : captions) {
                            List<InlineKeyboardButton> row = new ArrayList<>();
                            InlineKeyboardButton button = new InlineKeyboardButton();
                            button.setText("Слайд: " + caption);
                            button.setCallbackData("report_" + caption);
                            row.add(button);
                            buttons.add(row);
                        }
                        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
                        keyboardMarkup.setKeyboard(buttons);
                        sendMessage.setReplyMarkup(keyboardMarkup);

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("У вас нет прав для использования данной команды!");

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }else if("/most_ratings".equals(command)){
                reportHandler.handleMostRatingsCommand(message, sendMessage);
            }else if("/most_question".equals(command)){
                reportHandler.handleMostQuestionCommand(message, sendMessage);
            }else if("/most_response".equals(command)){
                reportHandler.handleMostResponseCommand(message, sendMessage);
            }else if("/height_ratings".equals(command)){
                reportHandler.handleHighestRatingsCommand(message, sendMessage);
            }


        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            long telegramUserId = message.getFrom().getId();
            String caption = update.getMessage().getCaption();
            if ("admin".equals(getUserRole(telegramUserId))) {
                if (!message.getPhoto().isEmpty()) {
                    String photoId = message.getPhoto().get(0).getFileId();
                    savePhotoInfo(photoId, caption);
                    InputFile photo = new InputFile(photoId);
                    List<Long> userIds = getAllRegisteredUsers();
                    for (Long userId : userIds) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(userId.toString());
                        sendPhoto.setPhoto(photo);
                        sendPhoto.setCaption(caption);
                        try {
                            execute(sendPhoto);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Фото не найдено");
                }
            }
        }else if(update.hasCallbackQuery()){
            handleCallbackQuery(update.getCallbackQuery());
        }

    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String data = callbackQuery.getData();
        String[] parts = data.split("_");
        if (parts[0].equals("report")) {
            String caption = parts[1];
            int photoId = getPhotoIdByCaption(caption);
            List<List<String>> report = getReport(photoId);
            String photoUrl = getPhotoUrl(photoId);

            StringBuilder reportMessage = new StringBuilder();
            reportMessage.append("Отчет по слайду: ").append(caption).append("\n\n");

            reportMessage.append("Вопросы:\n");
            for (String question : report.get(1)) {
                reportMessage.append(question).append("\n");
            }

            reportMessage.append("\nОтветы:\n");
            for (String response : report.get(0)) {
                reportMessage.append(response).append("\n");
            }

            reportMessage.append("\nОценки:\n");
            for (String rating : report.get(2)) {
                reportMessage.append(rating).append(" ");
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(callbackQuery.getMessage().getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photoUrl));
            sendPhoto.setCaption(reportMessage.toString());

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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
