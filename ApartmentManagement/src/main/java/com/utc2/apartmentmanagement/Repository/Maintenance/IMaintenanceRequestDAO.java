package com.utc2.apartmentmanagement.Repository.Maintenance;

import com.utc2.apartmentmanagement.Model.Maintenance.MaintenanceRequest;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IMaintenanceRequestDAO {
    List<MaintenanceRequest> getAllMaintenanceRequest();
    MaintenanceRequest getMaintenanceRequestByID(int requestID);
    boolean updateApartmentID(int requestID, int newApartmentID);
    boolean updateResidentID(int requestID, int newResidentID);
    boolean updateRequestDate(int requestID, Date newRequestDate);
    boolean updateDescription(int requestID, String newDescription);
    boolean updateStatusRequest(int requestID, String newStatus);
    boolean updateAssignedStaffId(int requestID, int newAssignStaffID);
    boolean updateCompletionDate(int requestID, Date newCompletionDate);
    boolean deleteMaintenanceRequestByID(int requestID);
    boolean addMaintenanceRequest(MaintenanceRequest maintenanceRequest);
    int countRequestByStatus(String status) throws SQLException;
    void saveMaintenaceRequest(String apartmentID, String residentID, LocalDate requestDate, String description,String priority, String issueType);
    List<Map<String, Object>> getMaintenanceRequestsByResidentId(int resient_id);
    int getTotalMaintenaceRequestByResidentID(int resident_id);

    List<Map<String, Object>> getFilterStatusAndPriority(int residentID, String status, String priority);

    List<Map<String, Object>> getMaintenanceRequestsByAssignedStaffId(int staffId);

    boolean updateStatus(int requestId, String inProgress);
}
