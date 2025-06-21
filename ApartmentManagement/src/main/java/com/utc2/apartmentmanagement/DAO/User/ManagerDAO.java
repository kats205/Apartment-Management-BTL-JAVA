package com.utc2.apartmentmanagement.DAO.User;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.User.Manager;
import com.utc2.apartmentmanagement.Repository.User.IManagerDAO;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ManagerDAO implements IManagerDAO {
    @Override
    public List<Manager>  getAllManager(){
        List<Manager> managerList = new ArrayList<>();
        String sql = "SELECT * FROM Manager";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                managerList.add(new Manager(rs.getInt("manager_id"), rs.getNString("office"), rs.getDate("start_date")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return managerList;
    }

    @Override
    public boolean addManger(Manager manager) {
        String sql = "INSERT INTO Manager(manger_id, office, start_date) VALUES( ? , ? , ?)";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, manager.getManagerID());
            stmt.setNString(2, manager.getOffice());
            stmt.setDate(3,manager.getStartDate());
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean  deleteManagerByID(int managerID){
        String deleteManagerSQL = "DELETE FROM Manager WHERE manger_id = ?";
        String deleteUserSQL = "DELETE FROM [User] WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(deleteUserSQL);
            PreparedStatement stmt1 = connection.prepareStatement(deleteManagerSQL);){
//            xóa dữ liệu trong bảng liên quan trước (user)
            stmt.setInt(1, managerID);
            stmt.executeUpdate();
//            xóa dữ liệu của manager
            stmt1.setInt(1, managerID);
            return stmt1.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean updateOffice(int managerID, String newOffice) {
        return updateManagerField(managerID, "office", newOffice);
    }

    @Override
    public boolean updateStartDate(int managerID, Date newStartDate) {
        return updateManagerField(managerID, "start_date", newStartDate);
    }


    @Override
    public Map<String, Object> getManagerByUserId(int userId) {
        String sql = "SELECT u.full_name, u.email, u.phone_number, m.office FROM [user] u\n" +
                "JOIN manager m ON u.user_id = m.manager_id\n" +
                "WHERE m.manager_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                Map<String, Object> information = new HashMap<>();
                information.put("full_name", rs.getString("full_name"));
                information.put("email", rs.getString("email"));
                information.put("phone_number", rs.getString("phone_number"));
                information.put("office", rs.getString("office"));
                return information;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean updateManagerField(int managerID, String field, Object newValue){
        String SQL = "UPDATE Manager SET " + field + " = ? WHERE manager_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL);){
            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date)newValue);
            }
            stmt.setInt(2, managerID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
