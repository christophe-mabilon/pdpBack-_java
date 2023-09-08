package com.b3al.spring.jwt.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.b3al.spring.jwt.mongodb.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

    void deleteByUsername ( String Username );
}
