package com.b3al.spring.jwt.mongodb.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "provider")
public class Provider {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    @Field(name = "siret")
    private String siret;

    @Field(name = "address")
    private AddressProvider address;

    @Field(name = "professionalMailAddress")
    private String professionalMailAddress;

    @Field(name = "professionalPhoneNumber")
    private String professionalPhoneNumber;

    @Field(name = "subjectToVAT")
    private boolean subjectToVAT;

    @Field(name = "intracommunityVATnumber")
    private String intracommunityVATnumber;

    @Field(name = "invoice")
    private Invoice invoice;

}
