package com.b3al.spring.jwt.mongodb.repository;


import com.b3al.spring.jwt.mongodb.models.Invoice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

}