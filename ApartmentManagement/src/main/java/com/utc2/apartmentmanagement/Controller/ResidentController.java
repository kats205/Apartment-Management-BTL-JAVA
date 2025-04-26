package com.utc2.apartmentmanagement.Controller;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ResidentController {
    public ResidentController(){

    }
    public void addResident(
                            String indentityCard,
                            String apartmentID,
                            String name,
                            Date Dob,
                            String Gender,
                            String phone,
                            String email,
                            boolean primaryResident) throws SQLException {
        String sql = "INSERT INTO Resident(identity_card, apartment_id, full_name," +
                " date_of_birth, gender, phone_number, email, is_primary_resident)" +
                " VALUES( ? , ? , ? , ? , ? , ? , ? , ? )";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(2, apartmentID);
            stmt.setString(1, indentityCard);
            stmt.setNString(3, name);
            stmt.setDate(4, Dob);
            stmt.setString(5, Gender);
            stmt.setString(6, phone);
            stmt.setString(7, email);
            stmt.setBoolean(8, primaryResident);
            int excuted = stmt.executeUpdate();
            if(excuted > 0){
                System.out.println("Add a resident successfully!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
