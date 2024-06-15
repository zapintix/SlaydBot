package org.example.DB.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository implements ReportDAO {
    public List<List<String>> getReport(int photoId) {
        List<List<String>> result = new ArrayList<>();
        // Get user responses for photoId
        List<String> responses = new ArrayList<>();
        String responsesSql = "SELECT username, response FROM user_responses WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
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

        // Get user questions for photoId
        List<String> questions = new ArrayList<>();
        String questionsSql = "SELECT username, question FROM user_questions WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
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

        // Get ratings for photoId
        List<String> ratings = new ArrayList<>();
        String ratingsSql = "SELECT rating FROM message_ratings WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
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

    public int getSlideWithMostResponses() {
        String sql = "SELECT photo_id FROM user_responses GROUP BY photo_id ORDER BY COUNT(response) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = AdminDataBase.connect();
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

    public int getSlideWithMostQuestions() {
        String sql = "SELECT photo_id FROM user_questions GROUP BY photo_id ORDER BY COUNT(question) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = AdminDataBase.connect();
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

    public int getSlideWithMostRatings() {
        String sql = "SELECT photo_id FROM message_ratings GROUP BY photo_id ORDER BY COUNT(rating) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = AdminDataBase.connect();
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

    public int getSlideWithHighestAverageRating() {
        String sql = "SELECT photo_id FROM message_ratings GROUP BY photo_id ORDER BY AVG(rating) DESC LIMIT 1";
        int photoId = -1;

        try (Connection conn = AdminDataBase.connect();
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



    public List<String> getRatings(int photoId) {
        List<String> ratings = new ArrayList<>();
        String sql = "SELECT rating FROM message_ratings WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
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

    public List<String> getQuestions(int photoId) {
        List<String> questions = new ArrayList<>();
        String sql = "SELECT question, username FROM user_questions WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String question = rs.getString("question");
                    String username = rs.getString("username");
                    questions.add(username + ": " + question);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public List<String> getResponses(int photoId) {
        List<String> responses = new ArrayList<>();
        String sql = "SELECT response, username FROM user_responses WHERE photo_id = ?";
        try (Connection conn = AdminDataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, photoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String response = rs.getString("response");
                    String username = rs.getString("username");
                    responses.add(username + ": " + response);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }

    public  double getAverageRating(int photoId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM message_ratings WHERE photo_id = ?";
        double averageRating = 0.0;
        try (Connection conn = AdminDataBase.connect();
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

}
