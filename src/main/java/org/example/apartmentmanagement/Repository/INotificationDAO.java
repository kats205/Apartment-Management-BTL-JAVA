package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Notification;

import java.util.List;

public interface INotificationDAO {
    List<Notification> getAllNotifications();
    Notification getNotificationById(int id);
    boolean addNotification(Notification notification);
    boolean deleteNotification(int id);
    boolean updateTitle(int id, String newTitle);
    boolean updateContent(int id, String newContent);
    boolean updateType(int id, String newType);

}
