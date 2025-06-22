package com.utc2.apartmentmanagement.Repository.Service;

import com.utc2.apartmentmanagement.Model.Service.ServiceRegistration;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IServiceRegistrationDAO{
    List<ServiceRegistration> getAllServiceRegistrations();
    ServiceRegistration getServiceRegistrationById(int id);
    boolean addServiceRegistration(int service_id, String apartment_id,Date start_date,Date end_date,String status,int registered_by,int resident_id);    boolean deleteServiceRegistration(int id);
    boolean updateStartDate(int id, Date newStartDate);
    boolean updateEndDate(int id, Date newEndDate);
    boolean updateStatus(int id, String newStatus);
    List<Map<String, Object>> getServiceRegistrationByApartmentId(String apartmentId) throws SQLException;
    int getServiceRegistrationCountByApartmentId(String apartmentId) throws SQLException;
}
