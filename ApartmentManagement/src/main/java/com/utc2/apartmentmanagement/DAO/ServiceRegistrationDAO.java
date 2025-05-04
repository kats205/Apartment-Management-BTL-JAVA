package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.ServiceRegistration;
import com.utc2.apartmentmanagement.Repository.IServiceRegistrationDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceRegistrationDAO implements IServiceRegistrationDAO {

    @Override
    public List<ServiceRegistration> getAllServiceRegistrations() {
        List<ServiceRegistration> serviceRegistrationList = new ArrayList<>();
        String sql = "SELECT * FROM ServiceRegistration";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                serviceRegistrationList.add(new ServiceRegistration(rs.getInt("registration_id"),
                        rs.getInt("service_id"),
                        rs.getInt("apartment_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return serviceRegistrationList;
    }

    @Override
    public ServiceRegistration getServiceRegistrationById(int id) {
        String sql = "SELECT * FROM ServiceRegistration WHERE registration_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ServiceRegistration(rs.getInt("registration_id"),
                        rs.getInt("service_id"),
                        rs.getInt("apartment_id"),
                        rs.getDate("start_date"),
                        rs.getDate("end_date"),
                        rs.getString("status"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addServiceRegistration(ServiceRegistration serviceRegistration) {
        String sql = "INSERT INTO ServiceRegistration (service_id, apartment_id, start_date, end_date, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, serviceRegistration.getServiceID());
            stmt.setInt(2, serviceRegistration.getApartmentID());
            stmt.setDate(3, new Date(serviceRegistration.getStartDate().getTime()));
            stmt.setDate(4, new Date(serviceRegistration.getEndDate().getTime()));
            stmt.setString(5, serviceRegistration.getStatus());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setDate(7, Date.valueOf(LocalDate.now()));
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteServiceRegistration(int id) {
        String sql = "DELETE FROM ServiceRegistration WHERE registration_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean updateStartDate(int id, Date newStartDate) {
        return updateServiceRegistrationField(id, "start_date", newStartDate);
    }

    @Override
    public boolean updateEndDate(int id, Date newEndDate) {
        return updateServiceRegistrationField(id, "end_date", newEndDate);
    }

    @Override
    public boolean updateStatus(int id, String newStatus) {
        return updateServiceRegistrationField(id, "status", newStatus);
    }

//    @Override
//    public int getTotalServiceRegistration(int apartmentID) {
//        String sql = "SELECT \n" +
//                "    COUNT(*) AS total_service_registrations\n" +
//                "FROM \n" +
//                "    ServiceRegistration\n" +
//                "WHERE \n" +
//                "    apartment_id = 'A101';";
//    }

    public boolean updateServiceRegistrationField(int id, String field, Object newValue){
        String sql = "UPDATE ServiceRegistration SET " + field + " = ?, updated_at = ? WHERE registration_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            if(newValue instanceof String){
                stmt.setNString(1, (String) newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Boolean){
                stmt.setBoolean(1, (Boolean) newValue);
            }
            else{
                throw new IllegalArgumentException("Invalid data type");
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
