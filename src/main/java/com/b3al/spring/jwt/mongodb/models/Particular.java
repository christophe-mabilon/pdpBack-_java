package com.b3al.spring.jwt.mongodb.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "particulars")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Particular {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String birthdate;
    private String professionalMailAddress;
    private String professionalPhoneNumber;
    private String phoneNumber;
    private String addressNumber;
    private String addressDetails;
    private String addressDetailsComplement;
    private String postalCode;
    private String city;
    private String country;

    @DBRef
    private User user;
}