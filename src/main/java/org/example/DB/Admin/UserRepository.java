package org.example.DB.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements UserDAO {
    public String getUserRole(long telegramUserId) {
        String role = null;
        String sql = "SELECT role FROM users WHERE tg_user_id = ?";

        try (Connection conn = AdminDataBase.connect();
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

    public List<Long> getAllRegisteredUsers() {
        List<Long> userIds = new ArrayList<>();
        String sql = "SELECT tg_user_id FROM users WHERE role = 'user'";

        try (Connection conn = AdminDataBase.connect();
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
}