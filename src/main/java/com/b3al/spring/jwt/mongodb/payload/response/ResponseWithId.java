package com.b3al.spring.jwt.mongodb.payload.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWithId {
    private String message;
    private String idNumber;

    public ResponseWithId ( String message , String additionalResponse ) {
        this.message = message;
        this.idNumber = additionalResponse ;
    }
}
