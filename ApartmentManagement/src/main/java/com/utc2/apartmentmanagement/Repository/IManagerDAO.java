package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Manager;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface IManagerDAO {
    List<Manager> getAllManager();
    boolean addManger(Manager manager);
    boolean deleteManagerByID(int managerID);
    boolean updateOffice(int managerID, String newOffice);
    boolean updateStartDate(int managerID, Date newStartDate);
    Map<String, Object> getManagerByUserId(int userId);
}
