package org.example;

import org.example.bot.ReportHandler;
import org.example.bot.TelegramBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBot telegramBot = new TelegramBot(); // Создаем бот без обработчика отчетов
            ReportHandler reportHandler = new ReportHandler(telegramBot); // Создаем обработчик отчетов и передаем ему бота
            telegramBot.setReportHandler(reportHandler); // Устанавливаем обработчик отчетов для бота
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
