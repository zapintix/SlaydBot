package org.example.bot;

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
import static org.example.DB.Admin.ReportRepository.*;
import static org.example.DB.Admin.UserRepository.getAllRegisteredUsers;
import static org.example.DB.Admin.UserRepository.getUserRole;

public class AdminReportHandler {
    private final TelegramBot bot;

    public AdminReportHandler(TelegramBot bot) {
        this.bot = bot;
    }


    public void handleMostRatingsCommand(Message message, SendMessage sendMessage) {
        long telegramUserId = message.getFrom().getId();
        if ("admin".equals(getUserRole(telegramUserId))) {
            int photoId = getSlideWithMostRatings();
            int count = 0;
            if (photoId != -1) {
                List<String> ratings = getRatings(photoId);
                String photoUrl = getPhotoUrl(photoId);
                String caption = getCaptionByPhotoId(photoId);

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
                    bot.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Не удалось найти слайд с оценками.");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("У вас нет прав для использования данной команды!");

            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }

    public void handleMostQuestionCommand(Message message, SendMessage sendMessage) {
        long telegramUserId = message.getFrom().getId();
        if ("admin".equals(getUserRole(telegramUserId))) {
            int photoId = getSlideWithMostQuestions();
            if (photoId != -1) {
                List<String> questions = getQuestions(photoId);
                String photoUrl = getPhotoUrl(photoId);
                String caption = getCaptionByPhotoId(photoId);

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
                    bot.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Не удалось найти слайд с вопросами.");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("У вас нет прав для использования данной команды!");

            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMostResponseCommand(Message message, SendMessage sendMessage) {
        long telegramUserId = message.getFrom().getId();
        if ("admin".equals(getUserRole(telegramUserId))) {
            int photoId = getSlideWithMostResponses();
            if (photoId != -1) {
                List<String> responses = getResponses(photoId);
                String photoUrl = getPhotoUrl(photoId);
                String caption = getCaptionByPhotoId(photoId);

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
                    bot.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Не удалось найти слайд с ответами.");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("У вас нет прав для использования данной команды!");

            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleHighestRatingsCommand(Message message, SendMessage sendMessage) {
        long telegramUserId = message.getFrom().getId();
        if ("admin".equals(getUserRole(telegramUserId))) {
            int photoId = getSlideWithHighestAverageRating();
            if (photoId != -1) {
                List<String> ratings = getRatings(photoId);
                String photoUrl = getPhotoUrl(photoId);
                String caption = getCaptionByPhotoId(photoId);

                double averageRating = getAverageRating(photoId);

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
                    bot.execute(sendPhoto);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Не удалось найти слайд с оценками.");
                try {
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("У вас нет прав для использования данной команды!");

            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public void SlaysReportCommand(Message message, SendMessage sendMessage) {
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
                    bot.execute(sendMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }else{
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("У вас нет прав для использования данной команды!");

            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
    public void SendPhotoToUsers(Message message, Update update){
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
                        bot.execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                System.out.println("Фото не найдено");
            }
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
                bot.execute(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}