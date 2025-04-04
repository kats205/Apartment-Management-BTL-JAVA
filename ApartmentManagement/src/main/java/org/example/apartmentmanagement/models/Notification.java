package org.example.apartmentmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Notification {
    private int notificationID;
    private String title;
    private String content;
    private Date creationDate;
    private String type;
    private boolean isRead;
    private List<Integer> recipientIDs;
}
