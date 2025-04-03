package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Building;

import java.sql.Date;
import java.util.List;

public interface IBuildingDAO {
    List<Building> getAllBuilding();
    Building getBuildingByID(int buildinID);
    boolean updateBuildingName(int buildingID, String newBuildingName);
    boolean updateAddress(int buildingID, String newAddress);
    boolean updateTotalFloor(int buildingID, int newToTalFloor);
    boolean updateTotalApartment(int buildingID, int newTotalApartment);
    boolean updateCompletionDate(int buildingID, Date newCompletionDate);
    boolean deleteBuildingbyID(int buildingID);
    boolean addBuilding(Building building);
}
