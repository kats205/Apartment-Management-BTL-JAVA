package com.utc2.apartmentmanagement.Model.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private int notifacationID;
    private String title;
    private String content;
    private Date creationDate;
    private String type;
    private boolean isRead;
//    private List<User> recipientIDs;



//    public boolean send (){
//
//    }
//    public void markAsRead(int userID){
//
//    }

}
