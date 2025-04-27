package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Notification;
import com.utc2.apartmentmanagement.Repository.INotificationDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO implements INotificationDAO {
    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> notificationList = new ArrayList<>();
        String sql = "SELECT * FROM notifications";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notificationList.add(new Notification(rs.getInt("notificationID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("creationDate"),
                        rs.getString("type"),
                        rs.getBoolean("isRead")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificationList;
    }

    @Override
    public Notification getNotificationById(int id) {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Notification(rs.getInt("notificationID"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("creationDate"),
                        rs.getString("type"),
                        rs.getBoolean("isRead"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addNotification(Notification notification) {
        String sql = "INSERT INTO notifications (title, content, creationDate, type, isRead, created_at, updated_at) VALUES (?, ?, ?, ?, ? , ? , ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, notification.getTitle());
            stmt.setString(2, notification.getContent());
            stmt.setDate(3, notification.getCreationDate());
            stmt.setString(4, notification.getType());
            stmt.setBoolean(5, notification.isRead());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteNotification(int id) {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTitle(int id, String newTitle) {
        return updateNotificationField(id, "title", newTitle);
    }

    @Override
    public boolean updateContent(int id, String newContent) {
        return updateNotificationField(id, "content", newContent);
    }

    @Override
    public boolean updateType(int id, String newType) {
        return updateNotificationField(id, "type", newType);
    }

    public boolean updateNotificationField(int id, String field, Object newValue) {
        String sql = "UPDATE notifications SET " + field + " = ? , updated_at = ? WHERE notification_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Boolean) {
                stmt.setBoolean(1, (Boolean) newValue);
            } else if (newValue instanceof Date) {
                stmt.setDate(1, (Date) newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
