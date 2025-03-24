package org.example.apartmentmanagement.Model.NotifacationAndReporting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.apartmentmanagement.Model.UserManager.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notifacation {
    private int notifacationID;
    private String title;
    private String content;
    private Date creationDate;
    private String type;
    private boolean isRead;
    private List<User> recipientIDs;



//    public boolean send (){
//
//    }
//    public void markAsRead(int userID){
//
//    }

}
