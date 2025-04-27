package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.Service;

import java.util.List;

public interface IServiceDAO {
    List<Service> getAllServices();
    Service getServiceById(int id);
    boolean addService(Service service);
    boolean deleteService(int id);
    boolean updateServiceName(int id, String newName);
    boolean updateServiceDescription(int id, String newDescription);
    boolean updateServicePrice(int id, double newPrice);
    boolean updateServiceUnit(int id, String newUnit);
    boolean updateServiceAvailability(int id, boolean isAvailable);
}
