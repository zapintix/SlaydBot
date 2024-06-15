package org.example.bot;
import org.example.DB.Admin.*;
import org.example.DB.User.Register;
import org.example.DB.User.RegisterDAO;
import org.example.DB.User.SavaReport;
import org.example.DB.User.SavaReportDAO;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
public class UserCommands {
    private final TelegramBot bot;
    private final PhotoDAO photoDAO;
    private final ReportDAO reportDAO;
    private final RegisterDAO registerDAO;
    private final SavaReportDAO savaReportDAO;
    private final UserDAO userDAO;
    private final KeyboardBuilder keyboardBuilder = new KeyboardBuilder();

    public UserCommands(TelegramBot bot) {
        this.bot = bot;
        this.photoDAO = new PhotoRepository();
        this.reportDAO = new ReportRepository();
        this.registerDAO = new Register();
        this.savaReportDAO = new SavaReport();
        this.userDAO = new UserRepository();
    }
    public void RegisterCommand(Message message, SendMessage sendMessage){
        sendMessage.setReplyMarkup(keyboardBuilder.getMainUserKeyboard());
        long telegramUserId = message.getFrom().getId();
        String username = message.getFrom().getFirstName();
        if (registerDAO.isUserRegistered(telegramUserId)) {
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText("Ошибка. Вы уже зарегистрированы");

        } else {
            registerDAO.registerUser(telegramUserId, username);
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
        if (registerDAO.isUserRegistered(telegramUserId)) {
            registerDAO.unregisterUser(telegramUserId);
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

        int photoId = photoDAO.getPhotoIdByCaption(caption);
        if(photoId!=-1) {
            if(response.length()==1 && response.matches("[1-5]")){
                int rating = Integer.parseInt(response);
                savaReportDAO.savePhotoRating(photoId,rating);
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Оценка: '" + rating + "'\nУспешно сохранена");
            }else if (response.endsWith("?")){
                savaReportDAO.saveUserQuestion(telegramName, response, photoId);
                sendMessage.setChatId(message.getChatId().toString());
                sendMessage.setText("Вопрос: '" + response + "'\nУспешно сохранён");
            }else{
                savaReportDAO.saveUserResponse(telegramName, response, photoId);
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
        if (registerDAO.isUserRegistered(telegramUserId)) {
            if ("user".equals(userDAO.getUserRole(telegramUserId))) {
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
