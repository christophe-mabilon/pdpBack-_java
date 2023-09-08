package com.b3al.spring.jwt.mongodb.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invoices")

public class Invoice {

    @Id
    private String id;
    private String fileId;
    private String invoiceNumber;
    private String purchaseOrder;
    private String workStartDate;
    private String deliveryDate;
    private Provider provider;
    private Client client;
    private String date;
    private Amount amount;
    private String paidAmount;
    private String paymentAdviceReference;
    private String jsonFormat;
    private Boolean validated;
    private User user;


}

