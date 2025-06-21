package com.utc2.apartmentmanagement.Repository.Complaint;

import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;

import java.time.LocalDate;
import java.util.List;

public interface IComplaintDAO {
    List<MaintenanceRequest> getAllMaintenanceRequest();
    void saveComplaintRequest(String apartmentID, String residentID, String complaintType, LocalDate requestDate, String description, String priority);

}
