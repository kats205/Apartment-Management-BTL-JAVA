package org.example.apartmentmanagement.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class Resident {
    private int residentID;
    private String apartmentID;
    private String fullName;
    private String identityCard;
    private Date dateOfBirth;
    private String gender;
    private int userID;
    private boolean isPrimaryResident;
    private Date moveInDate;
    private Date created_at;
    private Date updated_at;


    // hàm khởi tạo dùng để insert một resident xuống DB, Vì residentID được tạo tự động nên Constructor này không cần residentID
    public Resident(String apartmentID, String fullName, String identityCard, Date dateOfBirth,
                    String gender, int userID, boolean isPrimaryResident, Date moveInDate){
//        this.residentID = residentID;
        this.apartmentID = apartmentID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userID = userID;
        this.isPrimaryResident = isPrimaryResident;
        this.moveInDate = moveInDate;
    }
    // hàm khởi tạo này cần có residentID để lấy ra ID trong việc truy vấn select
    public Resident(int residentID, String apartmentID, String fullName, String identityCard, Date dateOfBirth,
                    String gender, int userID, boolean isPrimaryResident, Date moveInDate, Date created_at, Date updated_at){
        this.residentID = residentID;
        this.apartmentID = apartmentID;
        this.fullName = fullName;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.userID = userID;
        this.isPrimaryResident = isPrimaryResident;
        this.moveInDate = moveInDate;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

//    public void updateProfile(){
//
//    }
//    public Apartment getApartment(){
//
//    }
//
//    public List<Vehicles> getVehiclesList(){
//
//    }
}
