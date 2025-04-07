package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.NotificationRecipient;
import org.example.apartmentmanagement.Repository.INotificationRecipientDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NotificationRecipientDAO implements INotificationRecipientDAO {
    @Override
    public List<NotificationRecipient> getAllNotificationRecipients() {
        List<NotificationRecipient> notificationRecipientList = new ArrayList<>();
        String sql = "SELECT * FROM NotificationRecipient";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notificationRecipientList.add(new NotificationRecipient(
                        rs.getInt("notification_recipient_id"),
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("is_read"),
                        rs.getDate("read_at")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return notificationRecipientList;
    }

    @Override
    public NotificationRecipient getNotificationRecipientById(int id)  {
        String sql = "SELECT * FROM NotificationRecipient WHERE notification_recipient_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new NotificationRecipient(
                        rs.getInt("notification_recipient_id"),
                        rs.getInt("notification_id"),
                        rs.getInt("user_id"),
                        rs.getBoolean("is_read"),
                        rs.getDate("read_at")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addNotificationRecipient(NotificationRecipient notificationRecipient) {
        String sql = "INSERT INTO NotificationRecipient (notification_id, user_id, is_read, read_at, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, notificationRecipient.getNotifacationID());
            stmt.setInt(2, notificationRecipient.getUserID());
            stmt.setBoolean(3, notificationRecipient.isRead());
            stmt.setDate(4, notificationRecipient.getReadAt());
            stmt.setDate(5, Date.valueOf(LocalDate.now()));
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteNotificationRecipient(int id) {
        return updateNotificationRecipientField(id, "deleted_at", Date.valueOf(LocalDate.now()));
    }

    @Override
    public boolean updateIsRead(int id, boolean isRead) {
        return updateNotificationRecipientField(id, "is_read", isRead);
    }

    @Override
    public boolean updateReadDate(int id, String readDate) {
        return updateNotificationRecipientField(id, "read_at", Date.valueOf(readDate));
    }
    public boolean updateNotificationRecipientField(int id, String field, Object newValue){
        String sql = "UPDATE NotificationRecipient SET " + field + " = ? , updated_at = ? WHERE notification_recipient_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof String){
                stmt.setString(1, (String) newValue);
            }
            else if(newValue instanceof Boolean){
                stmt.setBoolean(1, (Boolean) newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            else{
                throw new IllegalArgumentException("Invalid data type");
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
