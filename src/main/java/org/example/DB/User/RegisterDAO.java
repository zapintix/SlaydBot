package org.example.DB.User;

public interface RegisterDAO {
    boolean isUserRegistered(long telegramUserId);
    void registerUser(long telegramUserId, String username);
    void unregisterUser(long telegramUserId);
}