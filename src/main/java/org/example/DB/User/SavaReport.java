
package org.example.DB.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.example.DB.User.UserDataBase.connect;

public class SavaReport implements SavaReportDAO{
    public void saveUserResponse(String telegramName, String response, int photoId) {
        String sql = "INSERT INTO user_responses (username, response, photo_id) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, telegramName);
            pstmt.setString(2, response);
            pstmt.setInt(3, photoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePhotoRating(int photoId, int rating) {
        String sql = "INSERT INTO message_ratings (photo_id, rating) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            pstmt.setInt(2, rating);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUserQuestion(String telegramName, String question, int photoId) {
        String sql = "INSERT INTO user_questions (username, question, photo_id) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, telegramName);
            pstmt.setString(2, question);
            pstmt.setInt(3, photoId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
