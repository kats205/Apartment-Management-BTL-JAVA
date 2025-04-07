package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.MaintenanceRequest;

import java.sql.Date;
import java.util.List;

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

}
