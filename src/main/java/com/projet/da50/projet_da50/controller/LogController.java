package com.projet.da50.projet_da50.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


// Create the class log too and do like Users
public class LogController {
    private final Connection connection;

    public LogController(Connection connection) {
        this.connection = connection;
    }

    public void saveLog(int userId, String action, String details) {
        String sql = "INSERT INTO logs (user_id, action, details) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setString(2, action);
            statement.setString(3, details);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
