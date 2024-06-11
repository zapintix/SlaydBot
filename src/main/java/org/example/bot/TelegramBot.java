package org.example.bot;

import org.example.DB.AdminDataBase;
import org.example.DB.UserDataBase;
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

import static org.example.DB.AdminDataBase.*;

public class TelegramBot extends TelegramLongPollingBot {

    private final KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
    private final UserDataBase userDataBase = new UserDataBase();
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
                if (userDataBase.isUserRegistered(telegramUserId)) {
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
                if (userDataBase.isUserRegistered(telegramUserId)) {
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
                if (userDataBase.isUserRegistered(telegramUserId)) {
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
                    List<String> captions = AdminDataBase.getAllSlideIds();
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
                handleMostRatingsCommand(message, sendMessage);
            }else if("/most_question".equals(command)){
                handleMostQuestionCommand(message, sendMessage);
            }else if("/most_response".equals(command)){
                handleMostResponseCommand(message, sendMessage);
            }else if("/height_ratings".equals(command)){
                handleHighestRatingsCommand(message, sendMessage);
            }


        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            long telegramUserId = message.getFrom().getId();
            String caption = update.getMessage().getCaption();
            if ("admin".equals(getUserRole(telegramUserId))) {
                if (!message.getPhoto().isEmpty()) {
                    String photoId = message.getPhoto().get(0).getFileId();
                    savePhotoInfo(photoId, caption);
                    InputFile photo = new InputFile(photoId);
                    List<Long> userIds = AdminDataBase.getAllRegisteredUsers();
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
            int photoId = AdminDataBase.getPhotoIdByCaption(caption);
            List<List<String>> report = AdminDataBase.getReport(photoId);
            String photoUrl = AdminDataBase.getPhotoUrl(photoId);

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
    private void handleMostRatingsCommand(Message message, SendMessage sendMessage) {
        int photoId = AdminDataBase.getSlideWithMostRatings();
        int count = 0;
        if (photoId != -1) {
            List<String> ratings = AdminDataBase.getRatings(photoId);
            String photoUrl = AdminDataBase.getPhotoUrl(photoId);
            String caption = AdminDataBase.getCaptionByPhotoId(photoId);

            StringBuilder reportMessage = new StringBuilder();
            reportMessage.append("Слайд с самым большим количеством оценок: ").append(caption).append("\n\n");

            reportMessage.append("Оценки:\n");
            for (String rating : ratings) {
                reportMessage.append(rating).append(" ");
                count++;
            }
            reportMessage.append("\nКол-во оценок:\n");
            reportMessage.append(count);

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photoUrl));
            sendPhoto.setCaption(reportMessage.toString());

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Не удалось найти слайд с оценками.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMostQuestionCommand(Message message, SendMessage sendMessage) {
        int photoId = AdminDataBase.getSlideWithMostQuestions();
        if (photoId != -1) {
            List<String> questions = AdminDataBase.getQuestions(photoId);
            String photoUrl = AdminDataBase.getPhotoUrl(photoId);
            String caption = AdminDataBase.getCaptionByPhotoId(photoId);

            StringBuilder reportMessage = new StringBuilder();
            reportMessage.append("Слайд с самым большим количеством вопросов: ").append(caption).append("\n\n");

            reportMessage.append("Вопросы:\n");
            for (String question : questions) {
                reportMessage.append(question).append("\n");
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photoUrl));
            sendPhoto.setCaption(reportMessage.toString());

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Не удалось найти слайд с вопросами.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMostResponseCommand(Message message, SendMessage sendMessage) {
        int photoId = AdminDataBase.getSlideWithMostResponses();
        if (photoId != -1) {
            List<String> responses = AdminDataBase.getResponses(photoId);
            String photoUrl = AdminDataBase.getPhotoUrl(photoId);
            String caption = AdminDataBase.getCaptionByPhotoId(photoId);

            StringBuilder reportMessage = new StringBuilder();
            reportMessage.append("Слайд с самым большим количеством ответов: ").append(caption).append("\n\n");

            reportMessage.append("Ответы:\n");
            for (String response : responses) {
                reportMessage.append(response).append("\n");
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photoUrl));
            sendPhoto.setCaption(reportMessage.toString());

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Не удалось найти слайд с ответами.");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleHighestRatingsCommand(Message message, SendMessage sendMessage) {
        int photoId = AdminDataBase.getSlideWithHighestAverageRating();
        if (photoId != -1) {
            List<String> ratings = AdminDataBase.getRatings(photoId);
            String photoUrl = AdminDataBase.getPhotoUrl(photoId);
            String caption = AdminDataBase.getCaptionByPhotoId(photoId);

            double averageRating = AdminDataBase.getAverageRating(photoId);

            StringBuilder reportMessage = new StringBuilder();
            reportMessage.append("Слайд с самой высокой средней оценкой: ").append(caption).append("\n\n");

            reportMessage.append("Средняя оценка: ").append(String.format("%.2f", averageRating)).append("\n\n");

            reportMessage.append("Оценки:\n");
            for (String rating : ratings) {
                reportMessage.append(rating).append(" ");
            }

            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setPhoto(new InputFile(photoUrl));
            sendPhoto.setCaption(reportMessage.toString());

            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Не удалось найти слайд с оценками.");
            try {
                execute(sendMessage);
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
