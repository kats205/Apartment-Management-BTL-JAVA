package org.example.apartmentmanagement.DAO;

import lombok.Getter;
import org.example.apartmentmanagement.Model.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerDAO {
    @Getter
    private static List<Manager> managerList = new ArrayList<>();
    public static void getAllManager(){
        managerList.clear();
        String sql = "SELECT * FROM Manager";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                managerList.add(new Manager(rs.getInt("manager_id"), rs.getNString("office"), rs.getDate("start_date")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void showAllManager(){
        if(managerList.isEmpty()) getManagerList();
        System.out.println("==============Manager List==============");
        for(Manager manager : managerList){
            System.out.println("---------------------------------");
            System.out.println("manager ID: " + manager.getManagerID());
            System.out.println("Office: " + manager.getOffice());
            System.out.println("Start Date: " + manager.getStartDate());
            System.out.println("---------------------------------");
        }
    }

    public static Manager findManagerByID(int managerID){
        if(managerID <= 0 ){
            System.out.println("invalid Manager ID");
            return null;
        }
        for(Manager manager : managerList){
            if(manager.getManagerID() == managerID) return manager;
        }
        return null;
    }

    public static void deleteManagerByID(int managerID){
        if(managerID <= 0) {
            System.out.println("Invalid Manager ID");
            return;
        }
        String deleteManagerSQL = "DELETE FROM Manager WHERE manger_id = ?";
        String deleteUserSQL = "DELETE FROM [User] WHERE user_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(deleteUserSQL);
            PreparedStatement stmt1 = connection.prepareStatement(deleteManagerSQL);
//            xóa dữ liệu trong bảng liên quan trước (user)
            stmt.setInt(1, managerID);
            stmt.executeUpdate();
//            xóa dữ liệu của manager
            stmt1.setInt(1, managerID);
            int executed = stmt1.executeUpdate();
            if(executed > 0){
                System.out.println("Delete Manager have Id = " + managerID + "Successfully!");
            }
            else{
                System.out.println("Delete fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateManagerByID(int managerID, String field, Object newValue){
        if(managerID <= 0 ){
            System.out.println("manager ID invalid!");
            return;
        }
        List<String> allowField = Arrays.asList("manager_id", "office", "start_date");
        if(!allowField.contains(field.toLowerCase())){
            System.out.println("Invalid field");
        }
        String SQL = "UPDATE Manager SET " + field + " = ? WHERE manager_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(SQL);

            if(newValue instanceof String){
                stmt.setNString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer)newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double)newValue);
            }

            stmt.setInt(2, managerID);
            int executed = stmt.executeUpdate();
            if(executed > 0){
                System.out.println("Update Successfully!");
            }
            else{
                System.out.println("Update fail!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
