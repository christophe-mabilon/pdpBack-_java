package com.b3al.spring.jwt.mongodb.service;

import com.b3al.spring.jwt.mongodb.models.User;
import com.b3al.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername ( String username ) {
        Optional<User> user = userRepository.findByUsername ( username );
        if (user.isPresent ( )) {
            return user;
        } else {
            return null;
        }

    }

    public Optional<User> getUserById ( String userName ) {
       return userRepository.findByUsername ( userName );
    }


}
