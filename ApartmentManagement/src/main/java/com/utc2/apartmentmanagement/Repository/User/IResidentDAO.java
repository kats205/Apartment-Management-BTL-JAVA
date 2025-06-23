package com.utc2.apartmentmanagement.Repository.User;

import com.utc2.apartmentmanagement.Model.User.Resident;

import java.sql.Date;
import java.util.List;

public interface IResidentDAO {
    List<Resident> getAllResident();
    Resident getResidentByID(int residentID);
    boolean deleteResidentByID(int residentID);
    boolean addResident(Resident resident);
    boolean updateApartmentID(int residentID, String newApartmentID);
    boolean updateFullName(int residentID, String newFullName);
    boolean updateIdentityCard(int residentID, String newIdentityCard);
    boolean updateDOB(int residentID, Date newDOB);
    boolean updateGender(int residentID, String newGender);
    boolean updateMoveInDate(int residentID, Date newMoveInDate);
    String getApartmentIdByUserID(int userID);
    Integer getResidentIDByUserID(int userID); // dùng Integer để trả về null
    List<Resident> searchOnChange(String searchText, String field);
}
