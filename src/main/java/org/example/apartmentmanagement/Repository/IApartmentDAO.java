package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.Model.Apartment;

import java.util.List;

public interface IApartmentDAO {
    List<Apartment> getAllApartments();
    Apartment findApartmentById(String apartmentID);
    boolean addApartment(Apartment apartment);
    boolean updateApartmentStatus(String apartmentID, String newStatus);
    boolean updateApartmentPrice(String apartmentID, double newPrice);
    boolean deleteApartmentById(String apartmentID);
}
