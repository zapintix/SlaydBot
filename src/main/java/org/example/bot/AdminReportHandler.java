package org.example.bot;

import org.example.DB.Admin.*;
import org.example.DB.User.Register;
import org.example.DB.User.RegisterDAO;
import org.example.DB.User.SavaReport;
import org.example.DB.User.SavaReportDAO;
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


public class AdminReportHandler {
    private final TelegramBot bot;
    private final PhotoDAO photoDAO;
    private final ReportDAO reportDAO;
    private final RegisterDAO registerDAO;
    private final SavaReportDAO savaReportDAO;
    private final UserDAO userDAO;
    public AdminReportHandler(TelegramBot bot) {
        this.bot = bot;
        this.photoDAO = new PhotoRepository();
        this.reportDAO = new ReportRepository();
        this.registerDAO = new Register();
        this.savaReportDAO = new SavaReport();
        this.userDAO =  new UserRepository(); // initialize userDAO here
    }

    public void handleMostRatingsCommand(Message message, SendMessage sendMessage) {
        long telegramUserId = message.getFrom().getId();
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            int photoId = reportDAO.getSlideWithMostRatings();
            int count = 0;
            if (photoId != -1) {
                List<String> ratings = reportDAO.getRatings(photoId);
                String photoUrl = photoDAO.getPhotoUrl(photoId);
                String caption = photoDAO.getCaptionByPhotoId(photoId);

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
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            int photoId = reportDAO.getSlideWithMostQuestions();
            if (photoId != -1) {
                List<String> questions = reportDAO.getQuestions(photoId);
                String photoUrl = photoDAO.getPhotoUrl(photoId);
                String caption = photoDAO.getCaptionByPhotoId(photoId);

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
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            int photoId = reportDAO.getSlideWithMostResponses();
            if (photoId != -1) {
                List<String> responses = reportDAO.getResponses(photoId);
                String photoUrl = photoDAO.getPhotoUrl(photoId);
                String caption = photoDAO.getCaptionByPhotoId(photoId);

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
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            int photoId = reportDAO.getSlideWithHighestAverageRating();
            if (photoId != -1) {
                List<String> ratings = reportDAO.getRatings(photoId);
                String photoUrl = photoDAO.getPhotoUrl(photoId);
                String caption = photoDAO.getCaptionByPhotoId(photoId);

                double averageRating = reportDAO.getAverageRating(photoId);

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
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            List<String> captions = photoDAO.getAllSlideIds();
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
        if ("admin".equals(userDAO.getUserRole(telegramUserId))) {
            if (!message.getPhoto().isEmpty()) {
                String photoId = message.getPhoto().get(0).getFileId();
                photoDAO.savePhotoInfo(photoId, caption);
                InputFile photo = new InputFile(photoId);
                List<Long> userIds = userDAO.getAllRegisteredUsers();
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
            int photoId = photoDAO.getPhotoIdByCaption(caption);
            List<List<String>> report = reportDAO.getReport(photoId);
            String photoUrl = photoDAO.getPhotoUrl(photoId);

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
