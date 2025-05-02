package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Apartment;

import java.sql.SQLException;
import java.util.List;

public interface IApartmentDAO {
    List<Apartment> getAllApartments();
    Apartment findApartmentById(String apartmentID);
    boolean addApartment(Apartment apartment);
    boolean updateApartmentStatus(String apartmentID, String newStatus);
    boolean updateApartmentPrice(String apartmentID, double newPrice);
    boolean deleteApartmentById(String apartmentID);
    boolean updateApartment(Apartment apartment) throws SQLException;
    List<Integer> getAllBuildingId() throws SQLException;
    List<Integer> getAllFloorId() throws SQLException;
    int countStatusApartment(String status) throws SQLException;
    List<Object> getApartmentInfoByApartmentID(String apartmentID);
}
