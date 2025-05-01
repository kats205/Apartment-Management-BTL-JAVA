package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Staff;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IStaffDAO {
    List<Staff> getAllStaff();
    boolean addStaff(Staff staff);
    boolean deleteStaffByID(int staffID);
    boolean updateDepartment(int staffID, String newDepartment);
    boolean updatePosition(int staffID, String newPosition);
    boolean updateHireDate(int staffID, Date newHireDate);
    boolean updateManagerID(int staffID, int newManagerID);
    List<Map<String, Object>> getAllStaffInfo() throws SQLException;
    List<String> listPosition() throws SQLException;
    List<Map<String, Object>> filterStaffByRoleName(String position) throws SQLException;
}
