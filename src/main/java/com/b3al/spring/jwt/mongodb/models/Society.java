package com.b3al.spring.jwt.mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "societies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Society {
    @Id
    private String id;
    private String societyName;
    private String siret;
    private String addressNumber;
    private String addressDetails;
    private String addressDetailsComplement;
    private String postalCode;
    private String city;
    private String country;
    private String email;
    private String phone;
    private Boolean subjectToVAT;
    private String intracommunityVATnumber;
    private String directorFirstName;
    private String directorLastName;
    private String birthdayDate;
    private String directorAddressNumber;
    private String directorAddressDetails;
    private String directorAddressDetailsComplement;
    private String directorPostalCode;
    private String directorCity;
    private String directorCountry;

    @DBRef
    private User user;
}