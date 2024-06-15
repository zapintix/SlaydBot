package org.example.DB.Admin;

import java.util.List;

public interface UserDAO {
    String getUserRole(long telegramUserId);
    List<Long> getAllRegisteredUsers();
}
