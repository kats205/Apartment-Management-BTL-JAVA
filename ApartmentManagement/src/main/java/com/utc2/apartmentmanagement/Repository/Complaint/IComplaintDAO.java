package com.utc2.apartmentmanagement.Repository.Complaint;

import com.utc2.apartmentmanagement.Model.Complaint.Complaint;
import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IComplaintDAO {
    void saveComplaintRequest(String apartmentID, String residentID, String complaintType, LocalDate requestDate, String description, String priority);
    List<Map<String, Object>>  getComplaintByResidentId(int residentID);

    int getTotalComplaintByResidentID(int residentID);

    List<Map<String, Object>> getFilterStatusAndType(int residentID, String status, String type);

    int getFilteredComplaintCount(int residentID, String status, String type);
    List<Complaint> getAllComplaints();
    boolean updateField(String field, Object value, int complaint_id);
    List<Complaint> getFilterStatusAndPriority(String field, String value);
}