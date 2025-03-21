package org.example.apartmentmanagement.Model.NotifacationAndReporting;

import org.example.apartmentmanagement.Model.UserManager.User;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Notifacation {
    private int notifacationID;
    private String title;
    private String content;
    private Date creationDate;
    private String type;
    private boolean isRead;
    private List<User> recipientIDs;

    public Notifacation() {
        notifacationID = 0;
        isRead = false;
        title = content = type = "";
        creationDate = null;
        recipientIDs = new ArrayList<User>();
    }

    public Notifacation(int notifacationID, String title, String content, Date creationDate, String type, boolean isRead, List<User> recepientIDs) {
        this.notifacationID = notifacationID;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.type = type;
        this.isRead = isRead;
        this.recipientIDs = recepientIDs;
    }

    public int getNotifacationID() {
        return notifacationID;
    }

    public void setNotifacationID(int notifacationID) {
        this.notifacationID = notifacationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public List<User> getRecipientIDs() {
        return recipientIDs;
    }

    public void setRecepientIDs(List<User> recepientIDs) {
        this.recipientIDs = recepientIDs;
    }

    public boolean send (){

    }
    public void markAsRead(int userID){

    }

}
