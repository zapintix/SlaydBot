package org.example.DB.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class PhotoRepository implements PhotoDAO {
    public int getPhotoIdByCaption(String caption) {
        String sql = "SELECT id FROM photos WHERE caption = ?";
        int photoId = -1;

        try (Connection conn = AdminDataBase.connect();
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

    public void savePhotoInfo(String photoId, String caption) {
        String sql = "INSERT INTO photos (photo_id, caption) VALUES (?, ?)";

        try (Connection conn = AdminDataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, photoId);
            pstmt.setString(2, caption);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getPhotoUrl(int photoId) {
        String sql = "SELECT photo_id FROM photos WHERE id = ?";
        String photoUrl = null;

        try (Connection conn = AdminDataBase.connect();
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

    public String getCaptionByPhotoId(int photoId) {
        String sql = "SELECT caption FROM photos WHERE id = ?";
        String caption = "";
        try (Connection conn = AdminDataBase.connect();
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

    public List<String> getAllSlideIds() {
        String sql = "SELECT caption FROM photos";
        List<String> captions = new ArrayList<>();
        try (Connection conn = AdminDataBase.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                captions.add(rs.getString("caption"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return captions;
    }
}
