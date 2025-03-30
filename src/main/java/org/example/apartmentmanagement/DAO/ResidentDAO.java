package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Resident;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ResidentDAO {
    private static List<Resident> residentList = new ArrayList<>();
    public static List<Resident> getAllResidentDAO() throws SQLException {
        residentList.clear();
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

    public static void showAllResident(){
        ResidentDAO residentDAO = new ResidentDAO();
        try {
            residentDAO.getAllResidentDAO(); // Lấy danh sách cư dân từ DB
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("===========================List Resident===========================");
        for(Resident resident : residentList){
            System.out.println("==================================================================");
            System.out.println("Resident ID: " + resident.getResidentID());
            System.out.println("Apartment ID: " + resident.getApartmentID());
            System.out.println("Full Name: " + resident.getFullName());
            System.out.println("Identity Card: " + resident.getIdentityCard());
            System.out.println("Date Of Birth: " + resident.getDateOfBirth());
            System.out.println("Gender: " + resident.getGender());
            System.out.println("User ID: " + resident.getUserID());
            if(resident.isPrimaryResident()) System.out.println(resident.getFullName() + " is a Resident at " + resident.getApartmentID());
            else System.out.println(resident.getFullName() + " isn't a Resident at " + resident.getApartmentID());
            System.out.println("Move In Date: " + resident.getMoveInDate());
            System.out.println("Created at: " + resident.getCreated_at());
            System.out.println("Updated at: " + resident.getUpdated_at());
            System.out.println("==================================================================");
        }
    }
    //thêm 1 resident vào DB
    public static void addAResident( String apartmentID, String fullName, String identityCard, Date Dob,
                             String gender, int userID, boolean isPrimaryResident, Date moveInDate){
        String sql = "INSERT INTO Resident( apartment_id, full_name, identity_card, date_of_birth, gender," +
                " user_id, is_primary_resident, move_in_date, created_at, updated_at) " +
                "values ( ? , ? , ? , ? , ? , ? , ? , ? , getdate(), getdate());";
        try{
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, apartmentID);
            stmt.setNString(2, fullName);
            stmt.setString(3, identityCard);
            stmt.setDate(4, Dob);
            stmt.setNString(5, gender);
            stmt.setInt(6, userID);
            stmt.setBoolean(7, isPrimaryResident);
            stmt.setDate(8, moveInDate);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) System.out.println("Add a resident successfully!");
            residentList.add(new Resident(apartmentID,fullName,identityCard,Dob,gender,userID,isPrimaryResident,moveInDate));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    // xóa resident bằng ID
    public static void deleteResidentById(int residentID) throws SQLException {
        String deleteMaintenanceSQL = "DELETE FROM MaintenanceRequest WHERE resident_id  = ?";
        String deleteResidentSQL = "DELETE FROM Resident WHERE resident_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt1 = connection.prepareStatement(deleteMaintenanceSQL);
             PreparedStatement stmt2 = connection.prepareStatement(deleteResidentSQL);
             Scanner sc = new Scanner(System.in)) {

            connection.setAutoCommit(false);

            System.out.println("Do you want to delete resident with ID " + residentID + "? (Yes/No)");
            String check = sc.nextLine().trim();
//            System.out.println(check);
            if (!check.equalsIgnoreCase("Yes")) {
                System.out.println("Deletion cancelled.");
                return;
            }
            stmt1.setInt(1, residentID);
            stmt1.executeUpdate();

            stmt2.setInt(1, residentID);
            int executed = stmt2.executeUpdate();

            if (executed > 0) {
                connection.commit(); // Chỉ commit nếu không có lỗi
                System.out.println("Delete A Resident Successfully!");
            } else {
                connection.rollback(); // Rollback nếu không có dòng nào bị xóa
                System.out.println("Resident Delete Failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (!DatabaseConnection.getConnection().getAutoCommit()) {
                    DatabaseConnection.getConnection().rollback(); // Rollback nếu có lỗi
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        }

    }
    // tìm resident bằng ID
    public static Resident findResidentById(int residentID) throws SQLException {
        if(residentList.isEmpty()){
            residentList = getAllResidentDAO();
        }
        for(Resident resident : residentList){
            if(resident.getResidentID() == residentID) return resident;
        }
        return null;
    }
    // hàm này dùng để cập nhật một resident thông qua id, khi chưa biết trước nên cập nhật thông tin gì
    // -> dùng Object để kiểm tra đối tượng cần update
    public static void updateResident(int residentID, String field, Object newValue) throws SQLException {
        // các column có sẵn trong Resident để tránh sql injection
        List<String> allowColumn = Arrays.asList("apartment_id", "full_name", "identity_card","date_of_birth", "gender", "user_id", "is_primary_resident", "move_in_date");
        if(!allowColumn.contains(field.toLowerCase())){
            System.out.println("Field need update isn't invalid!");
            return;
        }
        String sql = "UPDATE Resident SET " + field + " = ? WHERE resident_id = ?";
        String updated_atSQL = "UPDATE Resident SET updated_at = getdate() WHERE resident_id = ?";
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            PreparedStatement stmt1 = connection.prepareStatement(updated_atSQL);
            connection.setAutoCommit(false);
            if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double) newValue);
            }
            else if(newValue instanceof Date){
                stmt.setDate(1, (Date) newValue);
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setInt(2, residentID);
            int excuted = stmt.executeUpdate();
            if(excuted > 0) {
                stmt1.setInt(1, residentID);
                stmt1.executeUpdate();
                System.out.println("Update successfully!");
                connection.commit();
            }
            else System.out.println("Update fail!");
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
// Tạo các phương thức CRUD cho entity Resident thành công
}
