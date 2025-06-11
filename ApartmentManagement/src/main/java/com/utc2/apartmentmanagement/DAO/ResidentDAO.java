package com.utc2.apartmentmanagement.DAO;

import com.utc2.apartmentmanagement.Model.Resident;
import com.utc2.apartmentmanagement.Repository.IResidentDAO;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ResidentDAO implements IResidentDAO {
    @Override
    public List<Resident> getAllResident() {
        List<Resident> residentList = new ArrayList<>();
        String sql = "SELECT resident_id, apartment_id, full_name, identity_card, date_of_birth, gender, user_id, is_primary_resident, move_in_date, created_at, updated_at FROM Resident";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Resident resident = new Resident(rs.getInt("resident_id"),
                        rs.getString("apartment_id"),
                        rs.getString("full_name"),
                        rs.getString("identity_card"),
                        rs.getDate("date_of_birth"),
                        rs.getString("gender"),
                        rs.getInt("user_id"),
                        rs.getBoolean("is_primary_resident"),
                        rs.getDate("move_in_date"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at"));
                residentList.add(resident);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return residentList;
    }

    @Override
    public Resident getResidentByID(int residentID) {
        String sql = "SELECT * FROM Resident WHERE resident_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, residentID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Resident(rs.getInt("resident_id"),
                        rs.getString("apartment_id"),
                        rs.getString("full_name"),
                        rs.getString("identity_card"),
                        rs.getDate("date_of_birth"),
                        rs.getString("gender"),
                        rs.getInt("user_id"),
                        rs.getBoolean("is_primary_resident"),
                        rs.getDate("move_in_date"),
                        rs.getDate("created_at"),
                        rs.getDate("updated_at"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public String getApartmentIdByUserID(int userID) {
        String sql = "SELECT apartment_id from Resident WHERE user_id = ? ";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    @Override
    public Integer getResidentIDByUserID(int userID) {
        String sql = "SELECT resident_id FROM Resident WHERE [user_id] = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
            ){
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getInt("resident_id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    @Override
    public boolean deleteResidentByID(int residentID) {
        String deleteMaintenanceSQL = "DELETE FROM MaintenanceRequest WHERE resident_id  = ?";
        String deleteResidentSQL = "DELETE FROM Resident WHERE resident_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteMaintenanceSQL);
             PreparedStatement stmt2 = connection.prepareStatement(deleteResidentSQL);
             Scanner sc = new Scanner(System.in)) {

            System.out.println("Do you want to delete resident with ID " + residentID + "? (Yes/No)");
            String check = sc.nextLine().trim();
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return false;
            }
            stmt1.setInt(1, residentID);
            stmt1.executeUpdate();

            stmt2.setInt(1, residentID);
            return stmt2.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addResident(Resident resident) {
        String sql = "INSERT INTO Resident( apartment_id, full_name, identity_card, date_of_birth, gender," +
                " user_id, is_primary_resident, move_in_date, created_at, updated_at) " +
                "values ( ? , ? , ? , ? , ? , ? , ? , ? , getdate(), getdate());";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);){
            stmt.setString(1, resident.getApartmentID());
            stmt.setNString(2, resident.getFullName());
            stmt.setString(3, resident.getIdentityCard());
            stmt.setDate(4, resident.getDateOfBirth());
            stmt.setNString(5, resident.getGender());
            stmt.setInt(6, resident.getUserID());
            stmt.setBoolean(7, resident.isPrimaryResident());
            stmt.setDate(8, resident.getMoveInDate());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateApartmentID(int residentID, String newApartmentID) {
        return false;
    }

    @Override
    public boolean updateFullName(int residentID, String newFullName) {
        return false;
    }

    @Override
    public boolean updateIdentityCard(int residentID, String newIdentityCard) {
        return false;
    }

    @Override
    public boolean updateDOB(int residentID, Date newDOB) {
        return false;
    }

    @Override
    public boolean updateGender(int residentID, String newGender) {
        return false;
    }

    @Override
    public boolean updateMoveInDate(int residentID, Date newMoveInDate) {
        return false;
    }
    public boolean updateResidentField(int residentID, String field, Object newValue){
        String sql = "UPDATE Resident SET " + field + " = ? , updated_at = ? WHERE resident_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            connection.setAutoCommit(false);
            switch (newValue) {
                case String s -> stmt.setString(1, s);
                case Integer i -> stmt.setInt(1, i);
                case Double v -> stmt.setDouble(1, v);
                case Date date -> stmt.setDate(1, date);
                case null, default -> System.out.println("Field is invalid!");
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, residentID);
            int excuted = stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
// Tạo các phương thức CRUD cho entity Resident thành công
}
