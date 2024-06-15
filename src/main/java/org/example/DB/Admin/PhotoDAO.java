package org.example.DB.Admin;

import java.util.List;

public interface PhotoDAO {
    int getPhotoIdByCaption(String caption);
    void savePhotoInfo(String photoId, String caption);
    String getPhotoUrl(int photoId);
    String getCaptionByPhotoId(int photoId);
    List<String> getAllSlideIds();
}
