package com.b3al.spring.jwt.mongodb.controllers;

import com.b3al.spring.jwt.mongodb.models.ERole;
import com.b3al.spring.jwt.mongodb.models.Role;
import com.b3al.spring.jwt.mongodb.models.User;
import com.b3al.spring.jwt.mongodb.payload.request.LoginRequest;
import com.b3al.spring.jwt.mongodb.payload.request.SignupRequest;
import com.b3al.spring.jwt.mongodb.payload.request.UpdateRequest;
import com.b3al.spring.jwt.mongodb.payload.response.JwtResponse;
import com.b3al.spring.jwt.mongodb.payload.response.MessageResponse;
import com.b3al.spring.jwt.mongodb.repository.RoleRepository;
import com.b3al.spring.jwt.mongodb.repository.UserRepository;
import com.b3al.spring.jwt.mongodb.security.jwt.JwtUtils;
import com.b3al.spring.jwt.mongodb.security.services.UserDetailsImpl;
import com.b3al.spring.jwt.mongodb.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur : Le nom d'utilisateur est déjà pris !"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Erreur : L'email est déjà utilisé!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_NEW_USER)
                    .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "super-admin":
                        Role superAdminRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                        roles.add(superAdminRole);
                        break;
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                        roles.add(adminRole);
                        break;
                    case "client":
                        Role clientRole = roleRepository.findByName(ERole.ROLE_CLIENT)
                                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                        roles.add(clientRole);
                        break;
                    case "provider":
                        Role providerRole = roleRepository.findByName(ERole.ROLE_PROVIDER)
                                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                        roles.add(providerRole);
                        break;
                    default:
                        Role newUserRole = roleRepository.findByName(ERole.ROLE_NEW_USER)
                                .orElseThrow(() -> new RuntimeException("Erreur : Le rôle est introuvable."));
                        roles.add(newUserRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("L'utilisateur a été enregistré avec succès !"));
    }


}
