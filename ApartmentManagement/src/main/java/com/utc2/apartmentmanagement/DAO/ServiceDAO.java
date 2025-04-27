package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Service;
import com.utc2.apartmentmanagement.Repository.IServiceDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO implements IServiceDAO {
    @Override
    public List<Service> getAllServices() {
        List<Service> serviceList = new ArrayList<>();
        String sql = "SELECT * FROM Service";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                serviceList.add(new Service(rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("description"),
                        rs.getDouble("price_service"),
                        rs.getString("unit"),
                        rs.getBoolean("is_available")));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return serviceList;
    }

    @Override
    public Service getServiceById(int id) {
        String sql = "SELECT * FROM Service WHERE service_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Service(rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("description"),
                        rs.getDouble("price_service"),
                        rs.getString("unit"),
                        rs.getBoolean("is_available"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addService(Service service) {
        String sql = "INSERT INTO Service (service_name, description, price_service, unit, is_available) VALUES (?, ?, ?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, service.getServiceName());
            stmt.setString(2, service.getDescription());
            stmt.setDouble(3, service.getPrice());
            stmt.setString(4, service.getUnit());
            stmt.setBoolean(5, service.isAvailable());
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteService(int id) {
        String sql = "DELETE FROM Service WHERE service_id = ?";
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
    public boolean updateServiceName(int id, String newName) {
        return updateServiceField(id, "service_name", newName);
    }

    @Override
    public boolean updateServiceDescription(int id, String newDescription) {
        return updateServiceField(id, "description", newDescription);
    }

    @Override
    public boolean updateServicePrice(int id, double newPrice) {
        return updateServiceField(id, "price_service", newPrice);
    }

    @Override
    public boolean updateServiceUnit(int id, String newUnit) {
        return updateServiceField(id, "unit", newUnit);
    }

    @Override
    public boolean updateServiceAvailability(int id, boolean isAvailable) {
        return updateServiceField(id, "is_available", isAvailable);
    }
    public boolean updateServiceField(int id, String field, Object newValue){
        String sql = "UPDATE Service SET " + field + " = ?, updated_at = ? WHERE service_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            if(newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            } else if (newValue instanceof Boolean) {
                stmt.setBoolean(1, (Boolean) newValue);
            } else {
                throw new IllegalArgumentException("Unsupported data type");
            }
            stmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
            stmt.setInt(3, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
