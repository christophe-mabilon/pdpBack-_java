package com.b3al.spring.jwt.mongodb.controllers;


import com.b3al.spring.jwt.mongodb.models.Particular;
import com.b3al.spring.jwt.mongodb.repository.ParticularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/particular")
public class ParticularController {
    private final ParticularRepository particularRepository;

    @Autowired
    public ParticularController(ParticularRepository particularRepository) {
        this.particularRepository = particularRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Particular>> getParticularByUserId( @PathVariable Long userId) {
        Optional<Particular> particular = particularRepository.findByUserId(userId);

        if (particular != null ) {
            return ResponseEntity.ok(particular);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Optional<Particular>> updateParticular(@PathVariable Long userId, @RequestBody Particular updatedParticular) {
        Optional<Particular> particular = particularRepository.findByUserId(userId);

        if (particular.isPresent()) {

            // Mettre à jour les champs du particulier avec les valeurs fournies
            particular.get().setFirstName(updatedParticular.getFirstName());
            particular.get().setLastName(updatedParticular.getLastName());
            particular.get().setBirthdate(updatedParticular.getBirthdate());
            particular.get().setProfessionalMailAddress(updatedParticular.getProfessionalMailAddress());
            particular.get().setProfessionalPhoneNumber(updatedParticular.getProfessionalPhoneNumber());
            particular.get().setPhoneNumber(updatedParticular.getPhoneNumber());
            particular.get().setAddressNumber(updatedParticular.getAddressNumber());
            particular.get().setAddressDetails(updatedParticular.getAddressDetails());
            particular.get().setAddressDetailsComplement(updatedParticular.getAddressDetailsComplement());
            particular.get().setPostalCode(updatedParticular.getPostalCode());
            particular.get().setCity(updatedParticular.getCity());
            particular.get().setCountry(updatedParticular.getCountry());

            particularRepository.save(particular.get()); // Enregistrer les modifications dans la base de données

            return ResponseEntity.ok(particular);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}