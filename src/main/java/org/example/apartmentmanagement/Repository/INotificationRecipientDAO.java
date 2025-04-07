package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.NotificationRecipient;

import java.util.List;

public interface INotificationRecipientDAO {
    List<NotificationRecipient> getAllNotificationRecipients();
    NotificationRecipient getNotificationRecipientById(int id);
    boolean addNotificationRecipient(NotificationRecipient notificationRecipient);
    boolean deleteNotificationRecipient(int id);
    boolean updateIsRead(int id, boolean isRead);
    boolean updateReadDate(int id, String readDate);
}
