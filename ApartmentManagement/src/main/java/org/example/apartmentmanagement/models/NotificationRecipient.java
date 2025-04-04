package org.example.apartmentmanagement.models;

import java.sql.Date;

public class NotificationRecipient {
    private int notifacationRecipientID;
    private int notifacationID;
    private int userID;
    private boolean isRead;
    private Date readAt;
}
