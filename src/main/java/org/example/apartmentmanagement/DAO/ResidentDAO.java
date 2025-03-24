package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.ResidentManager.Resident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResidentDAO {
    public List<Resident> getAllResidentDAO() throws SQLException {
        String sql = "select resident_id, apartment_id, full_name, identity_card, date_of_birth, gender, phone_number, email from Resident";
        List<Resident> residentList = new ArrayList<>();
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
                        rs.getString("phone_number"),
                        rs.getString("email"));
                residentList.add(resident);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return residentList;
    }

}
