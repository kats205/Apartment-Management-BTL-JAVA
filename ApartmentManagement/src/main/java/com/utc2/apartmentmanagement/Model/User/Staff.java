package com.utc2.apartmentmanagement.Model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff extends User{
    private int staffID;
    private String department; // văn phòng làm việc của nhân viên
    private String position; // vị trí làm việc
    private Date hireDate; // ngày nhân viên được tuyển dụng
    private int managerID; // người quản lý trực tiếp nhân viên

}
