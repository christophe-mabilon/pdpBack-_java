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
@Document(collection = "amount")
public class Amount {
    @Id
    private String id;
    private Float totalAmountTva;
    private Float totalAmountHT;
    private Float totalAmountTTC;
    @Field("invoice")
    private Invoice invoice;

}