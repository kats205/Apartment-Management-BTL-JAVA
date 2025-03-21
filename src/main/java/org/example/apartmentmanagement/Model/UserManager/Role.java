package org.example.apartmentmanagement.Model.UserManager;

import java.util.ArrayList;
import java.util.List;

public class Role {
    private int roleID;
    private String roleName;
    private String description;
    private List<String> permissions;

    public Role() {
        roleID = 0;
        roleName = description = "";
        permissions = new ArrayList<String>();
    }

    public Role(int roleID, String roleName, String description, List<String> permissions) {
        this.roleID = roleID;
        this.roleName = roleName;
        this.description = description;
        this.permissions = permissions;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }


    public void addPermission(String permission){
        permissions.add(permission);
    }
    public void removePermission(String permission){
        permissions.remove(permission);
    }
    @Override
    public String toString() {
        return "Role{" +
                "roleID=" + roleID +
                ", roleName='" + roleName + '\'' +
                ", description='" + description + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
