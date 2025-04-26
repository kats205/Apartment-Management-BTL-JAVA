package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Staff;

import java.sql.Date;
import java.util.List;

public interface IStaffDAO {
    List<Staff> getAllStaff();
    boolean addStaff(Staff staff);
    boolean deleteStaffByID(int staffID);
    boolean updateDepartment(int staffID, String newDepartment);
    boolean updatePosition(int staffID, String newPosition);
    boolean updateHireDate(int staffID, Date newHireDate);
    boolean updateManagerID(int staffID, int newManagerID);
}
