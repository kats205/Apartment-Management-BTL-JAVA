package com.utc2.apartmentmanagement.Repository;

import com.utc2.apartmentmanagement.Model.ServiceRegistration;

import java.sql.Date;
import java.util.List;

public interface IServiceRegistrationDAO {
    List<ServiceRegistration> getAllServiceRegistrations();
    ServiceRegistration getServiceRegistrationById(int id);
    boolean addServiceRegistration(ServiceRegistration serviceRegistration);
    boolean deleteServiceRegistration(int id);
    boolean updateStartDate(int id, Date newStartDate);
    boolean updateEndDate(int id, Date newEndDate);
    boolean updateStatus(int id, String newStatus);
  //  int getTotalServiceRegistration(int apartmentID);

}
