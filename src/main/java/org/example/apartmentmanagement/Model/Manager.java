package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manager extends User{
    private int managerID;
    private String office; // văn phòng làm việc
    private Date startDate; // ngày bổ nhiệm
}
