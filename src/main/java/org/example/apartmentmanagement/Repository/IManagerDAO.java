package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Manager;

import java.sql.Date;
import java.util.List;

public interface IManagerDAO {
    List<Manager> getAllManager();
    boolean addManger(Manager manager);
    boolean deleteManagerByID(int managerID);
    boolean updateOffice(int managerID, String newOffice);
    boolean updateStartDate(int managerID, Date newStartDate);
}
