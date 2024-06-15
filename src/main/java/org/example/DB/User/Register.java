package org.example.DB.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.example.DB.User.UserDataBase.connect;

public class Register implements RegisterDAO {

    public boolean isUserRegistered(long telegramUserId)  {
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

    public void registerUser(long telegramUserId, String username) {
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
    public void unregisterUser(long telegramUserId) {
        String sql = "DELETE FROM users WHERE tg_user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, telegramUserId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
