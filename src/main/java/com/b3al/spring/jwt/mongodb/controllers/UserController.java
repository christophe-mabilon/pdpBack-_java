package com.b3al.spring.jwt.mongodb.controllers;

import com.b3al.spring.jwt.mongodb.models.ERole;
import com.b3al.spring.jwt.mongodb.models.Role;
import com.b3al.spring.jwt.mongodb.models.User;
import com.b3al.spring.jwt.mongodb.payload.response.MessageResponse;
import com.b3al.spring.jwt.mongodb.repository.RoleRepository;
import com.b3al.spring.jwt.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    public ResponseEntity<User> getUser ( @PathVariable("username") String username ) {
        Optional<User> user = userRepository.findByUsername ( username );
        if (user.isPresent ( )) {
            return new ResponseEntity<> ( user.get ( ) , HttpStatus.OK );
        } else {
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND );
        }
    }


    @PutMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody User userRequest) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update username if provided in the request
            if (userRequest.getUsername() != null && !userRequest.getUsername().equals(user.getUsername())) {
                user.setUsername(userRequest.getUsername());
            }

            // Update email if provided in the request
            if (userRequest.getEmail() != null && !userRequest.getEmail().equals(user.getEmail())) {
                user.setEmail(userRequest.getEmail());
            }

            // Update roles if provided in the request
            if (userRequest.getRoles() != null && !userRequest.getRoles().isEmpty()) {
                Set<Role> requestedRoles = userRequest.getRoles();
                Set<Role> roles = new HashSet<>();

                requestedRoles.forEach(role -> {
                    ERole enumRole = role.getName();
                    if (enumRole == null) {
                        throw new RuntimeException("Erreur : Le rôle est introuvable.");
                    }

                    Role userRole = roleRepository.findByName(enumRole)
                            .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                    roles.add(userRole);
                });

                user.setRoles(roles);
            }

            userRepository.save(user);
            return ResponseEntity.ok(new MessageResponse("L'utilisateur a été mis à jour avec succès !"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{username}")
   // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    public ResponseEntity<MessageResponse> deleteUser ( @PathVariable String username ) {
        Optional<User> user = userRepository.findByUsername ( username );
        if (user.isPresent ( )) {
            userRepository.deleteByUsername ( user.get ( ).getUsername ( ) );
            return ResponseEntity.ok ( new MessageResponse ( "L'utilisateur a été supprimé avec succès !" ) );
        } else {
            return ResponseEntity.notFound ( ).build ( );
        }
    }
}
