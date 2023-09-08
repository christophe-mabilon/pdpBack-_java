package com.b3al.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "client")
public class Client {
    @Id
    private String id;
    private String name;
    @Nullable
    private String lastName;
    @Nullable
    private String birthdate;
    @Nullable
    private String siret;
    private AddressClient address;
    private String professionalMailAddress;
    private String professionalPhoneNumber;
    private boolean subjectToVAT;
    private String intracommunityVATnumber;
    private Invoice invoice;

}