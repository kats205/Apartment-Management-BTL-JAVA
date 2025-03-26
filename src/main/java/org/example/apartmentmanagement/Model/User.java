package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userID;
    private String userName;
    private String passWord;
    private String fullName;
    private String email;
    private String phoneNumber;
    private int roleID;
    private boolean active;
}
