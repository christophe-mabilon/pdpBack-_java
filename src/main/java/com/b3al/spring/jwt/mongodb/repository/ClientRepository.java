package com.b3al.spring.jwt.mongodb.repository;

import com.b3al.spring.jwt.mongodb.models.Client;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends MongoRepository<Client, Long> {
}
