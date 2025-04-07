package org.example.apartmentmanagement.DAO;

import org.example.apartmentmanagement.Model.Role;
import org.example.apartmentmanagement.Repository.IRoleDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO implements IRoleDAO {
    @Override
    public List<Role> getAllRoles() {
        List<Role> roleList = new ArrayList<>();
        String sql = "SELECT * FROM role";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                roleList.add(new Role(
                        rs.getInt("role_id"),
                        rs.getString("role_name"),
                        rs.getString("description")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return roleList;
    }

    @Override
    public Role getRoleById(int id) {
        String sql = "SELECT * FROM role WHERE role_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new Role(
                        rs.getInt("role_id"),
                        rs.getString("role_name"),
                        rs.getString("description")
                );
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addRole(Role role) {
        String sql = "INSERT INTO role (role_id, role_name, description) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, role.getRoleID());
            stmt.setString(2, role.getRoleName());
            stmt.setString(3, role.getDescription());
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRole(int id) {
        String sql = "DELETE FROM role WHERE role_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateRoleName(int id, String newName) {
        return updateRoleField(id, "role_name", newName);
    }

    @Override
    public boolean updateRoleDescription(int id, String newDescription) {
        return updateRoleField(id, "description", newDescription);
    }
    public boolean updateRoleField(int id, String field, Object newValue) {
        String sql = "UPDATE role SET " + field + " = ? WHERE role_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (newValue instanceof String) {
                stmt.setNString(1, (String) newValue);
            } else if (newValue instanceof Double) {
                stmt.setDouble(1, (Double) newValue);
            } else if (newValue instanceof Date) {
                stmt.setDate(1, (Date) newValue);
            }
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
