package org.example.apartmentmanagement.Model.UserManager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    private int roleID;
    private String roleName;
    private String description;
    private List<String> permissions;

}
