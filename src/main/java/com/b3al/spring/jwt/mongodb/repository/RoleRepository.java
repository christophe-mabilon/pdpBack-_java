package com.b3al.spring.jwt.mongodb.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.b3al.spring.jwt.mongodb.models.ERole;
import com.b3al.spring.jwt.mongodb.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
