package com.b3al.spring.jwt.mongodb.repository;

import com.b3al.spring.jwt.mongodb.models.InvoiceFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvoiceFileRepository extends MongoRepository<InvoiceFile, String> {


    Optional<InvoiceFile> findByUserId ( String userId );
}