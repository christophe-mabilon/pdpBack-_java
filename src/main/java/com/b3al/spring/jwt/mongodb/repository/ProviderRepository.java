package com.b3al.spring.jwt.mongodb.repository;


import com.b3al.spring.jwt.mongodb.models.Provider;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProviderRepository extends MongoRepository<Provider, Long> {
}
