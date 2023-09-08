package com.b3al.spring.jwt.mongodb.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "AddressClient")
public class AddressClient {
    @Id
    private String id;
    private String number;
    private String addressDetails;
    private String postalCode;
    private String city;
    private String country;
    @Field("client")
    private Client client;

}