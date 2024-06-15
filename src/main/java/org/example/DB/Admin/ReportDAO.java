
package org.example.DB.Admin;

import java.util.List;

public interface ReportDAO {
    List<List<String>> getReport(int photoId);
    int getSlideWithMostResponses();
    int getSlideWithMostQuestions();
    int getSlideWithMostRatings();
    int getSlideWithHighestAverageRating();
    List<String> getRatings(int photoId);
    List<String> getQuestions(int photoId);
    List<String> getResponses(int photoId);
    double getAverageRating(int photoId);
}
