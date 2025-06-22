package com.utc2.apartmentmanagement.Repository.Complaint;

import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IComplaintDAO {
    List<MaintenanceRequest> getAllMaintenanceRequest();

    List<Map<String, Object>>  getComplaintByResidentId(int residentID);

    int getTotalComplaintByResidentID(int residentID);

    void saveComplaintRequest(String apartmentID, String residentID, String complaintType, LocalDate requestDate, String description, String priority);

    List<Map<String, Object>> getFilterStatusAndType(int residentID, String status, String type);

    int getFilteredComplaintCount(int residentID, String status, String type);
}
