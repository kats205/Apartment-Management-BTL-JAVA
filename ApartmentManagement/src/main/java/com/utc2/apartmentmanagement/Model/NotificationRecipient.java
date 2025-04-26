package com.utc2.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRecipient {
    private int notifacationRecipientID;
    private int notifacationID;
    private int userID;
    private boolean isRead;
    private Date readAt;

}

