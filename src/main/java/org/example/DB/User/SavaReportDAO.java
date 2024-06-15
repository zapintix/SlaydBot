package org.example.DB.User;

public interface SavaReportDAO {
    void saveUserResponse(String telegramName, String response, int photoId);
    void savePhotoRating(int photoId, int rating);
    void saveUserQuestion(String telegramName, String question, int photoId);
}
