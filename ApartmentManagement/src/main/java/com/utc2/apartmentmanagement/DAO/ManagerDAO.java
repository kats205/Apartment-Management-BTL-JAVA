package com.utc2.apartmentmanagement.DAO;

import lombok.Getter;
import com.utc2.apartmentmanagement.Model.Manager;
import com.utc2.apartmentmanagement.Repository.IManagerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
