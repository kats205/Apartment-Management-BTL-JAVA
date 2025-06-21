package com.utc2.apartmentmanagement.DAO.Apartment;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.Apartment.Apartment;
import com.utc2.apartmentmanagement.Repository.Apartment.IApartmentDAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ApartmentDAO implements IApartmentDAO {
    @Override
    public List<Apartment> getAllApartments() {
        List<Apartment> apartments = new ArrayList<>();
        String sql = "SELECT * FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                apartments.add(new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartments;
    }
    public List<String> getAllIdApartment() {
        List<String> apartmentIdList = new ArrayList<>();
        String sql = "SELECT apartment_id FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                apartmentIdList.add(rs.getString("apartment_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartmentIdList;
    }
    public List<String> getAllStatus() {
        List<String> StatusList = new ArrayList<>();
        String sql = "SELECT distinct [status] FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StatusList.add(rs.getString("status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return StatusList;
    }
    public static void showAllApartment(){
        List<Apartment> apartmentList = new ApartmentDAO().getAllApartments();
        if(!apartmentList.isEmpty()){
            for(Apartment apartment : apartmentList){
                System.out.println("Aparment ID: " + apartment.getApartmentID());
                System.out.println("Building ID: " + apartment.getBuildingID());
                System.out.println("Floors: " + apartment.getFloors());
                System.out.println("Area: " + apartment.getArea());
                System.out.println("Bedroom: " + apartment.getBedRoom());
                System.out.println("Price Apartment: " + apartment.getPriceApartment());
                System.out.println("Status: " + apartment.getStatus());
                System.out.println("Mainenance Fee: " + apartment.getMaintanceFee());
            }
        }
    }
    @Override
    public Apartment findApartmentById(String apartmentID) {
        String sql = "SELECT * FROM Apartment WHERE apartment_id = ?";
        // sử dụng try-with-resource để sau khi thực hiện xong truy vấn thì tự động ngắt kết nối với DB
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartmentID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Apartment findApartmentByIdAndStatus(String apartmentID, String status) {
        String sql = "SELECT * FROM Apartment WHERE apartment_id = ? AND status = ?";
        // sử dụng try-with-resource để sau khi thực hiện xong truy vấn thì tự động ngắt kết nối với DB
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartmentID);
            stmt.setString(2,status);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Apartment> findApartmentByStatus(String status) {
        String sql = "SELECT * FROM Apartment WHERE status = ?";
        // sử dụng try-with-resource để sau khi thực hiện xong truy vấn thì tự động ngắt kết nối với DB
        List<Apartment> apartmentList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                apartmentList.add( new Apartment(
                        rs.getString("apartment_id"),
                        rs.getInt("building_id"),
                        rs.getInt("floor"),
                        rs.getFloat("area"),
                        rs.getInt("bedrooms"),
                        rs.getDouble("price_apartment"),
                        rs.getString("status"),
                        rs.getDouble("maintenance_fee")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apartmentList;
    }

    @Override
    public boolean addApartment(Apartment apartment) {
        String sql = "INSERT INTO Apartment(apartment_id, building_id, floor, area, bedrooms, price_apartment, status, maintenance_fee, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartment.getApartmentID());
            stmt.setInt(2, apartment.getBuildingID());
            stmt.setInt(3, apartment.getFloors());
            stmt.setDouble(4, apartment.getArea());
            stmt.setInt(5, apartment.getBedRoom());
            stmt.setDouble(6, apartment.getPriceApartment());
            stmt.setString(7, apartment.getStatus());
            stmt.setDouble(8, apartment.getMaintanceFee());
            stmt.setDate(9, Date.valueOf(LocalDate.now()));
            stmt.setDate(10, Date.valueOf(LocalDate.now()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateApartmentStatus(String apartmentID, String newStatus) {
        return updateApartmentField(apartmentID, "status", newStatus);
    }

    @Override
    public boolean updateApartmentPrice(String apartmentID, double newPrice) {
        return updateApartmentField(apartmentID, "price_apartment", newPrice);
    }

    private boolean updateApartmentField(String apartmentID, String field, Object newValue) {
        String sql = "UPDATE Apartment SET " + field + " = ?, updated_at = ? WHERE apartment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                stmt.setString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setString(3, apartmentID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteApartmentById(String apartmentID) {
        String sql = "DELETE FROM Apartment WHERE apartment_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, apartmentID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateApartment(Apartment apartment) throws SQLException {
        String sql = "UPDATE Apartment SET  area = ?, bedrooms = ?, price_apartment = ?, status = ?, maintenance_fee = ?, updated_at = ? WHERE apartment_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setDouble(1, apartment.getArea());
            stmt.setInt(2, apartment.getBedRoom());
            stmt.setDouble(3, apartment.getPriceApartment());
            stmt.setString(4, apartment.getStatus());
            stmt.setDouble(5, apartment.getMaintanceFee());
            stmt.setDate(6, Date.valueOf(LocalDate.now()));
            stmt.setString(7, apartment.getApartmentID());
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            throw new SQLException("Lỗi khi cập nhật căn hộ", e);
        }
    }

    @Override
    public List<Integer> getAllBuildingId() throws SQLException{
        List<Integer> buildingIdList = new ArrayList<>();
        String sql = "SELECT distinct building_id FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                buildingIdList.add(rs.getInt("building_id"));
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách ID tòa nhà", e);
        }
        return buildingIdList;
    }

    @Override
    public List<Integer> getAllFloorId() throws SQLException {
        List<Integer> floorList = new ArrayList<>();
        String sql = "SELECT distinct floor FROM Apartment";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                floorList.add(rs.getInt("floor"));
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy danh sách tầng", e);
        }
        return floorList;
    }

    @Override
    public int countStatusApartment(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Apartment WHERE status = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(SQLException e){
            throw new SQLException("Lỗi khi đếm số lượng căn hộ theo trạng thái", e);
        }
        return 0;
    }

    @Override
    public List<Object> getApartmentInfoByApartmentID(String apartmentID) {
        String sql = "SELECT a.building_id, a.floor, a.area FROM Apartment a WHERE a.apartment_id = ?;";
        List<Object> objectList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,apartmentID);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                objectList.add(rs.getInt("building_id"));
                objectList.add(rs.getInt("floor"));
                objectList.add(rs.getDouble("area"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return objectList;
    }

    @Override
    public Map<String, Object> getInformation(int userId) throws SQLException {
        String sql = "SELECT a.apartment_id, a.status, a.floor, a.area, a.bedrooms, a.maintenance_fee,\n" +
                "\t\tb.address, a.price_apartment, r.move_in_date, r.full_name, r.resident_id\n" +
                "FROM Resident r\n" +
                "JOIN Apartment a ON r.apartment_id = a.apartment_id\n" +
                "JOIN Building b ON a.building_id = b.building_id\n" +
                "WHERE r.user_id = ? ";
        Map<String, Object> map = new HashMap<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                map.put("apartment_id", rs.getString("apartment_id"));
                map.put("status", rs.getString("status"));
                map.put("floor", rs.getInt("floor"));
                map.put("area", rs.getDouble("area"));
                map.put("bedrooms", rs.getInt("bedrooms"));
                map.put("maintenance_fee", rs.getDouble("maintenance_fee"));
                map.put("address", rs.getString("address"));
                map.put("price_apartment", rs.getDouble("price_apartment"));
                map.put("move_in_date", rs.getDate("move_in_date"));
                map.put("full_name", rs.getString("full_name"));
                map.put("resident_id", rs.getInt("resident_id"));
            }
        }catch (SQLException e){
            throw new SQLException("Lỗi khi lấy thông tin căn hộ", e);
        }
        return map;
    }


    public int countApartment() throws SQLException{
        String sql = "SELECT COUNT(*) FROM Apartment";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            throw new SQLException("Lỗi khi đếm số lượng căn hộ", e);
        }
        return 0;
    }

    public static void main(String[] args) throws SQLException {
        Map<String, Object> map = new ApartmentDAO().getInformation(11);
        for(Map.Entry<String, Object> entry : map.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
