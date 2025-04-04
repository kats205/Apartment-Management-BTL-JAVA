package org.example.apartmentmanagement.Repository;

import org.example.apartmentmanagement.models.Apartment;

import java.util.List;

public interface iApartment {
    List<Apartment> getAllApartments();
    void showAllApartment();
    boolean addApartment(Apartment apartment);
    boolean deleteApartmentById(String apartmentID);


}
