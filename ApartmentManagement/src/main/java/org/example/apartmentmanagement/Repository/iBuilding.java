package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.models.Building;

import java.util.List;

public interface iBuilding {
    List<Building> getAllBuilding();
    void showAllBuilding();
    boolean addBuilding(Building building);
    boolean deleteBuildingbyID(int buildingID);
}
