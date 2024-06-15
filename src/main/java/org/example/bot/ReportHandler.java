package org.example.bot;

import org.example.DB.Admin.AdminDataBase;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

import static org.example.DB.Admin.PhotoRepository.getCaptionByPhotoId;
import static org.example.DB.Admin.PhotoRepository.getPhotoUrl;
import static org.example.DB.Admin.ReportRepository.*;

public class ReportHandler {
    private final TelegramBot bot;

    public ReportHandler(TelegramBot bot) {
        this.bot = bot;
    }


    public void handleMostRatingsCommand(Message message, SendMessage sendMessage) {
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
    }

    public void handleMostQuestionCommand(Message message, SendMessage sendMessage) {
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
    }

    public void handleMostResponseCommand(Message message, SendMessage sendMessage) {
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
    }

    public void handleHighestRatingsCommand(Message message, SendMessage sendMessage) {
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
    }
}
