package org.example.apartmentmanagement.DAOLayer;

import org.example.apartmentmanagement.Repository.iApartment;
import org.example.apartmentmanagement.models.Apartment;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ApartmentDAO implements iApartment {


    @Override
    public List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM Apartment";
      //  try (Connection connection = DatabaseConnection)
        return apartments;
    }

    @Override
    public boolean addApartment(Apartment apartment) {
        return false;
    }

    @Override
    public boolean deleteApartmentById(String apartmentID) {
        return false;
    }
}
