package com.utc2.apartmentmanagement.Repository.User;

import com.utc2.apartmentmanagement.Model.User.Role;

import java.util.List;

public interface IRoleDAO {
    List<Role> getAllRoles();
    Role getRoleById(int id);
    boolean addRole(Role role);
    boolean deleteRole(int id);
    boolean updateRoleName(int id, String newName);
    boolean updateRoleDescription(int id, String newDescription);

}
