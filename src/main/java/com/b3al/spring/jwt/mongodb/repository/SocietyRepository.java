package com.b3al.spring.jwt.mongodb.repository;

import com.b3al.spring.jwt.mongodb.models.Society;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface SocietyRepository extends MongoRepository<Society, Long> {
   Optional<Society> findByUserId ( Long userId );
}
