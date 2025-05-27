package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.ServiceRegistration;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServiceRegistrationDAO{
    List<ServiceRegistration> getAllServiceRegistrations();
    ServiceRegistration getServiceRegistrationById(int id);
    boolean addServiceRegistration(ServiceRegistration serviceRegistration);
    boolean deleteServiceRegistration(int id);
    boolean updateStartDate(int id, Date newStartDate);
    boolean updateEndDate(int id, Date newEndDate);
    boolean updateStatus(int id, String newStatus);
    List<Map<String, Object>> getServiceRegistrationByApartmentId(String apartmentId) throws SQLException;
    int getServiceRegistrationCountByApartmentId(String apartmentId) throws SQLException;
}
