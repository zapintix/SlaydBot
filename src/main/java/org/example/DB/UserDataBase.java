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

}

