package com.b3al.spring.jwt.mongodb.models;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


@Data
@NoArgsConstructor
@Document(collection = "invoicesFile")
public class InvoiceFile {
    private String id;

    private String userId;
    private String invoiceNumber;
    private String pdfFileB64;//Contenu du fichier en base64
    private Date creationDate;//Date de creation du fichier
    private Date destructionDate;//Date de destruction du fichier
    private String modifiedByUserId;


    public InvoiceFile ( String id , String userId, String invoiceNumber , String pdfFileB64 , Date creationDate , Date destructionDate, String modifiedByUserId ) {
        this.id = id;
        this.userId=userId;
        this.invoiceNumber = invoiceNumber;
        this.pdfFileB64 = pdfFileB64;
        this.creationDate = creationDate;
        this.destructionDate = destructionDate;
        this.modifiedByUserId = modifiedByUserId;
    }

    @JsonCreator
    public InvoiceFile ( String userId, @JsonProperty("invoiceNumber") String invoiceNumber , @JsonProperty("pdfFileB64") String pdfFileB64, String modifiedByUserId ) {
        this.userId=userId;
        this.invoiceNumber = invoiceNumber;
        this.pdfFileB64 = pdfFileB64;
        this.creationDate = new Date ( );
        this.destructionDate = Date.from ( LocalDate.now ( ).plusMonths ( 1 ).atStartOfDay ( ZoneId.systemDefault ( ) ).toInstant ( ) );
        this.modifiedByUserId = modifiedByUserId;
    }
}

