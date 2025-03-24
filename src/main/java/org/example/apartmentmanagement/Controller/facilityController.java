//package org.example.apartmentmanagement.Controller;
//
//import org.example.apartmentmanagement.DAO.FacilityDAO;
//
//import java.math.BigDecimal;
//import java.sql.Date;
//import java.sql.SQLException;
//import java.util.Scanner;
//
//public class facilityController {
//    public static void main(String[] args) throws SQLException {
//        FacilityDAO facilityDAO = new FacilityDAO();
//
//        facilityDAO.createFacility("A004", "Hồ Bơi", "2025-03-01", "2025-03-10", true);
//        facilityDAO.createFacility("B003", "Hồ bơi", "2024-03-20", "2024-03-21", true);
//        facilityDAO.getAllFacilily();
//        Scanner sc = new Scanner(System.in);
//        String Id = sc.nextLine();
//        facilityDAO.deleteFacilityByID(Id);
//    }
//}
