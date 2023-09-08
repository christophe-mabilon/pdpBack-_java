package com.b3al.spring.jwt.mongodb.repository;

import com.b3al.spring.jwt.mongodb.models.Particular;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface ParticularRepository extends MongoRepository<Particular, Long> {

    Optional<Particular> findByUserId ( Long aLong );
}
