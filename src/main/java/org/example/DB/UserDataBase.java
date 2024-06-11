package org.example.DB;

import java.sql.*;


public class UserDataBase {
    private static final String URL = "jdbc:postgresql://localhost:5432/Slayd";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public boolean isUserRegistered(long telegramUserId) {
        String sql = "SELECT COUNT(*) FROM users WHERE tg_user_id = ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, telegramUserId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveUserResponse(String telegramName, String response, int photoId) {
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

    public static void savePhotoRating(int photoId, int rating) {
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

    public static void saveUserQuestion(String telegramName, String question, int photoId) {
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

