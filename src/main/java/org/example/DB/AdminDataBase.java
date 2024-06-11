package org.example.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDataBase {
    private static final String URL = "jdbc:postgresql://localhost:5432/Slayd";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void registerUser(long telegramUserId, String username) {
        String sql = "INSERT INTO users (tg_user_id, username) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, telegramUserId);
            pstmt.setString(2, username);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void unregisterUser(long telegramUserId) {
        String sql = "DELETE FROM users WHERE tg_user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, telegramUserId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static int getPhotoIdByCaption(String caption) {
        String sql = "SELECT id FROM photos WHERE caption = ?";
        int photoId = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, caption);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    photoId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoId;
    }



    public static String getUserRole(long telegramUserId) {
        String role = null;
        String sql = "SELECT role FROM users WHERE tg_user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, telegramUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    public static void savePhotoInfo(String photoId, String caption) {
        String sql = "INSERT INTO photos (photo_id, caption) VALUES (?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, photoId);
            pstmt.setString(2, caption);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Long> getAllRegisteredUsers() {
        List<Long> userIds = new ArrayList<>();
        String sql = "SELECT tg_user_id FROM users WHERE role = 'user'";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                userIds.add(rs.getLong("tg_user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userIds;
    }

    public static List<String> getAllSlideIds() {
        String sql = "SELECT caption FROM photos";
        List<String> caption = new ArrayList<>();
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                caption.add(rs.getString("caption"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caption;
    }

    public static List<List<String>> getReport(int photoId) {
        List<List<String>> result = new ArrayList<>();
        //Метод для получения отчёта
        // Получаем ответы пользователей по photoId
        List<String> responses = new ArrayList<>();
        String responsesSql = "SELECT username, response FROM user_responses WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(responsesSql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String response = rs.getString("response");
                    responses.add(username + ": " + response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result.add(responses);

        // Получаем вопросы пользователей по photoId
        List<String> questions = new ArrayList<>();
        String questionsSql = "SELECT username, question FROM user_questions WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(questionsSql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    String question = rs.getString("question");
                    questions.add(username + ": " + question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result.add(questions);

        // Получаем оценки по photoId
        List<String> ratings = new ArrayList<>();
        String ratingsSql = "SELECT rating FROM message_ratings WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(ratingsSql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                int totalRating = 0;
                int count = 0;
                while (rs.next()) {
                    int rating = rs.getInt("rating");
                    totalRating += rating;
                    ratings.add(String.valueOf(rating));
                    count++;
                }
                if (count > 0) {
                    double averageRating = (double) totalRating / count;
                    ratings.add("\nСредняя оценка: " + String.format("%.2f", averageRating));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result.add(ratings);

        return result;
    }

    public static String getPhotoUrl(int photoId) {
        String sql = "SELECT photo_id FROM photos WHERE id = ?";
        String photoUrl = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    photoUrl = rs.getString("photo_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoUrl;
    }

    public static int getSlideWithMostResponses() {
        String sql = "SELECT photo_id FROM user_responses GROUP BY photo_id ORDER BY COUNT(response) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                photoId = rs.getInt("photo_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoId;
    }

    public static int getSlideWithMostQuestions() {
        String sql = "SELECT photo_id FROM user_questions GROUP BY photo_id ORDER BY COUNT(question) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                photoId = rs.getInt("photo_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoId;
    }

    public static int getSlideWithMostRatings() {
        String sql = "SELECT photo_id FROM message_ratings GROUP BY photo_id ORDER BY COUNT(rating) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                photoId = rs.getInt("photo_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoId;
    }

    public static int getSlideWithHighestAverageRating() {
        String sql = "SELECT photo_id FROM message_ratings GROUP BY photo_id ORDER BY AVG(rating) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                photoId = rs.getInt("photo_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return photoId;
    }

    public static List<String> getRatings(int photoId) {
        List<String> ratings = new ArrayList<>();
        String sql = "SELECT rating FROM message_ratings WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ratings.add(rs.getString("rating"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public static List<String>  getQuestions(int photoId) {
        List<String> questions = new ArrayList<>();
        String sql = "SELECT question, username  FROM user_questions WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String question = rs.getString("question");
                    String username = rs.getString("username");
                    questions.add(username + ": "+question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public static List<String> getResponses(int photoId) {
        List<String> responses = new ArrayList<>();
        String sql = "SELECT response, username FROM user_responses WHERE photo_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String response = rs.getString("response");
                    String username = rs.getString("username");
                    responses.add(username + ": "+response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public static double getAverageRating(int photoId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM message_ratings WHERE photo_id = ?";
        double averageRating = 0.0;
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    averageRating = rs.getDouble("avg_rating");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return averageRating;
    }

    public static String getCaptionByPhotoId(int photoId) {
        String sql = "SELECT caption FROM photos WHERE id = ?";
        String caption = "";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    caption = rs.getString("caption");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return caption;
    }

}



