package com.utc2.apartmentmanagement.DAO.User;

import com.utc2.apartmentmanagement.DAO.DatabaseConnection;
import com.utc2.apartmentmanagement.Model.User.User;
import com.utc2.apartmentmanagement.Repository.User.IUserDAO;
import com.utc2.apartmentmanagement.Utils.passwordEncryption;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.utc2.apartmentmanagement.Utils.AlertBox.showAlert;


public class UserDAO implements IUserDAO {
    public static List<String> getAllValuesofColumn(String columnName) {
        List<String> resultList = new ArrayList<>();
        String sql = "SELECT " + columnName + " FROM [User]";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection != null ? connection.prepareStatement(sql) : null;
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                resultList.add(rs.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Cảnh báo!", "Lỗi kết nối database!");
        }

        return resultList;
    }
    @Override
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * from [User]";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();){
            while(rs.next()){
                userList.add(new User(rs.getInt("user_id"), rs.getString( "username"),rs.getString( "password"), rs.getNString( "full_name"),
                        rs.getString( "email"), rs.getString( "phone_number"), rs.getInt( "role_id"), rs.getBoolean( "active")));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public User getUserByID(int userID) {
        String sql = "SELECT * from [User] WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1,userID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new User(rs.getInt("user_id"), rs.getString( "username"),rs.getString( "password"), rs.getString( "full_name"),
                        rs.getString( "email"), rs.getString( "phone_number"), rs.getInt( "role_id"), rs.getBoolean( "active"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addUser(User user) {
        String sql = "INSERT INTO [User](username, password, full_name, email, phone_number, role_id, active, created_at, updated_at) VALUES ( ? , ? , ? , ? , ? , ? , ? , getdate() , getdate())";
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            conn.setAutoCommit(false);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, passwordEncryption.hashPassword(user.getPassWord()));
            stmt.setNString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhoneNumber());
            stmt.setInt(6, user.getRoleID());
            stmt.setBoolean(7, user.isActive());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit(); // Commit khi insert thành công
                return true;
            } else {
                conn.rollback(); // Rollback nếu không insert được
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUserByID(int userID) {
        String sql = "DELETE FROM [User] WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            stmt.setInt(1, userID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            throw new RuntimeException();
        }
    }

    @Override
    public boolean updateUserName(int userID, String newUserName) {
        return updateStaffField(userID, "username", newUserName);
    }

    @Override
    public boolean updatePassWord(int userID, String newPassword) {
        return updateStaffField(userID, "password", newPassword);
    }

    @Override
    public boolean updateFullName(int userID, String newFullName) {
        return updateStaffField(userID, "full_name", newFullName);
    }

    @Override
    public boolean updateEmail(int userID, String newEmail) {
        return updateStaffField(userID, "email", newEmail);
    }

    @Override
    public boolean updatePhoneNumber(int userID, String newPhoneNumber) {
        return updateStaffField(userID, "phone_number", newPhoneNumber);
    }

    @Override
    public boolean updateRoleID(int userID, int newRoleID) {
        return updateStaffField(userID, "role_id", newRoleID);
    }

    @Override
    public boolean updateActive(int userID, boolean newActive) {
        return updateStaffField(userID, "active", newActive);
    }

    @Override
    public int getIdByUserName(String userName) throws SQLException {
        String sql = "SELECT user_id FROM [User] WHERE username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi lấy user id từ username", e);
        }
        return 0;
    }

    public List<Map<String, Object>> searchOnChange(String searchText) throws SQLException {
        String sql = "SELECT s.staff_id, u.username, r.role_name, u.full_name, " +
                "u.email, u.phone_number, s.department, s.position, u.active " +
                "FROM staff s " +
                "JOIN [user] u ON u.user_id = s.staff_id " +
                "JOIN role r ON r.role_id = u.role_id " +
                "WHERE u.full_name LIKE ?";

        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ID", rs.getInt("staff_id"));
                row.put("Tendangnhap", rs.getString("username"));
                row.put("Vaitro", rs.getString("role_name"));
                row.put("Hoten", rs.getString("full_name"));
                row.put("Email", rs.getString("email"));
                row.put("Sodienthoai", rs.getString("phone_number"));
                row.put("Phongban", rs.getString("department"));
                row.put("Chucvu", rs.getString("position"));
                row.put("Trangthai", rs.getBoolean("active"));
                result.add(row);
            }
        }
        return result;
    }

    @Override
    public boolean updateAvatar(int user_id, String filePath) throws SQLException {
        String sql = "UPDATE [User] SET avatar_filename = ? WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(2, user_id);
            stmt.setString(1, filePath);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            throw new SQLException("Lỗi khi cập nhật avatar", e);
        }
    }

    @Override
    public String getAvatarPathByUserId(String userName) throws SQLException {
        String sql = "SELECT avatar_filename FROM [User] WHERE username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getString("avatar_filename");
            }
        }catch (SQLException e){
            throw new SQLException("Lỗi khi lấy đường dẫn avatar", e);
        }
        return "";
    }


    public int login (String userName, String passWord){
        String sql = "SELECT * FROM [User] WHERE username = ?";
        int roleID = 0;
        if (userName == null || userName.isEmpty() || passWord == null || passWord.isEmpty()) {
            return 0; // Tránh truy vấn SQL nếu giá trị rỗng
        }
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                System.out.println("Stored Hash: " + hashedPassword);
                System.out.println("Input Password: " + passWord);

                boolean isMatch = passwordEncryption.checkPassword(passWord, hashedPassword);
                System.out.println("Password Match: " + isMatch);

                if (true) {
                    roleID = rs.getInt("role_id");
                    System.out.println("User role: " + roleID);
                } else {
                    System.out.println("Password does not match!");
                }
            } else {
                System.out.println("No user found with this username.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleID;
    }

    public static boolean updateStaffField(int userID, String field, Object newValue){
        String sql = "UPDATE [User] SET " + field + " = ? , updated_at = ? WHERE user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);){
            if(newValue instanceof String){
                stmt.setString(1, (String)newValue);
            }
            else if(newValue instanceof Integer){
                stmt.setInt(1, (Integer) newValue);
            }
            else if(newValue instanceof Double){
                stmt.setDouble(1, (Double) newValue);
            }
            else if(newValue instanceof Boolean){
                stmt.setInt(1, Boolean.compare((Boolean)newValue, false));
            }
            else{
                System.out.println("Field is invalid!");
            }
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setInt(3, userID);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public String getPasswordByUserId(int userId) {
        String sql = "SELECT password FROM [User] WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE [User] SET password = ? WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, passwordEncryption.hashPassword(newPassword));
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getUserIdBvEmail(String email) {
        String sql = "SELECT user_id FROM [User] WHERE email = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
        }catch(SQLException e){
            System.out.println("Đã xảy ra lỗi khi lấy user id");
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean updateLastLogin(String userName, LocalDateTime lastLogin) {
        String sql = "UPDATE [User] Set last_login = ? WHERE username = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setTimestamp(1, Timestamp.valueOf(lastLogin));
            stmt.setString(2, userName);
            return stmt.executeUpdate() > 0;
        }catch(SQLException e){
            System.out.println("Đã xảy ra lỗi khi cập nhật thời gian đăng nhập " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String, String>> listUserLastLogin() {
        String sql = "SELECT \n" +
                "    u.username,\n" +
                "    r.role_name as Role,\n" +
                "    CASE WHEN u.active = 1 THEN 'Active' ELSE 'Inactive' END as Status,\n" +
                "    'Login' as Action,\n" +
                "    'User Authentication' as Activity,\n" +
                "    CAST(u.last_login as DATE) as Date,\n" +
                "    FORMAT(u.last_login, 'HH:mm:ss') as Time\n" +
                "FROM [User] u\n" +
                "JOIN Role r ON u.role_id = r.role_id\n" +
                "WHERE u.last_login IS NOT NULL\n" +
                "ORDER BY u.last_login DESC";
        List<Map<String,String>> list = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, String > rows = new HashMap<>();
                rows.put("username", rs.getString("username"));
                rows.put("Role", rs.getString("Role"));
                rows.put("Action", rs.getString("action"));
                rows.put("Activity", rs.getString("activity"));
                rows.put("Status", rs.getString("Status"));
                rows.put("Date", rs.getString("date"));
                rows.put("Time", rs.getString("time"));
                list.add(rows);
            }
        }catch(SQLException e){
            System.out.println("Đã xảy ra lỗi trong quá trình lấy thông tin user đăng nhập!!"+e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map<String, String>> listUserAssignTasks() {
        String sql = "SELECT s.staff_id, u.email, u.full_name, u.phone_number FROM [User] u JOIN Staff s ON s.staff_id = u.user_id";
        List<Map<String, String>> list = new ArrayList<>();
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Map<String, String> rows = new HashMap<>();
                rows.put("staff_id", rs.getString("staff_id"));
                rows.put("email", rs.getString("email"));
                rows.put("full_name", rs.getString("full_name"));
                rows.put("phone_number", rs.getString("phone_number"));
                list.add(rows);
            }
        }catch(SQLException e){
            System.out.println("Đã xảy ra lỗi trong quá trình lấy thông tin user phân công nhiệm vụ!!" + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}
