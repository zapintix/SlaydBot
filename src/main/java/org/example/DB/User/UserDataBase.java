package org.example.DB.User;

import java.sql.*;


public class UserDataBase {
    private static final String URL = "jdbc:postgresql://localhost:5432/Slayd";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "admin";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }





}

