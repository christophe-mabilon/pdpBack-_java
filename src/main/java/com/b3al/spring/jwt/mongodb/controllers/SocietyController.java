package com.b3al.spring.jwt.mongodb.controllers;



import com.b3al.spring.jwt.mongodb.models.Society;
import com.b3al.spring.jwt.mongodb.repository.SocietyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/societies")
public class SocietyController {
    private final SocietyRepository societyRepository;

    @Autowired
    public SocietyController ( SocietyRepository societyRepository ) {
        this.societyRepository = societyRepository;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Optional<Society>> getSocietyByUserId ( @PathVariable Long userId ) {
        Optional<Society> society = societyRepository.findByUserId ( userId );

        if (society.isPresent ( )) {
            return ResponseEntity.ok ( society );
        }

        return ResponseEntity.status ( HttpStatus.NOT_FOUND ).build ( );
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Optional<Society>> updateSociety ( @PathVariable Long userId , @RequestBody Society updatedSociety ) {
      Optional<Society> society = societyRepository.findByUserId ( userId );

        if (society.isPresent ()) {
            // Mise à jour les champs de la société avec les valeurs fournies
            society.get().setSocietyName ( updatedSociety.getSocietyName ( ) );
            society.get().setSiret ( updatedSociety.getSiret ( ) );
            society.get().setAddressNumber ( updatedSociety.getAddressNumber ( ) );
            society.get().setAddressDetails ( updatedSociety.getAddressDetails ( ) );
            society.get().setAddressDetailsComplement ( updatedSociety.getAddressDetailsComplement ( ) );
            society.get().setPostalCode ( updatedSociety.getPostalCode ( ) );
            society.get().setCity ( updatedSociety.getCity ( ) );
            society.get().setCountry ( updatedSociety.getCountry ( ) );
            society.get().setEmail ( updatedSociety.getEmail ( ) );
            society.get().setPhone ( updatedSociety.getPhone ( ) );
            society.get().setSubjectToVAT ( updatedSociety.getSubjectToVAT ( ) );
            society.get().setIntracommunityVATnumber ( updatedSociety.getIntracommunityVATnumber ( ) );
            society.get().setDirectorFirstName ( updatedSociety.getDirectorFirstName ( ) );
            society.get().setDirectorLastName ( updatedSociety.getDirectorLastName ( ) );
            society.get().setBirthdayDate ( updatedSociety.getBirthdayDate ( ) );
            society.get().setDirectorAddressNumber ( updatedSociety.getDirectorAddressNumber ( ) );
            society.get().setDirectorAddressDetails ( updatedSociety.getDirectorAddressDetails ( ) );
            society.get().setDirectorAddressDetailsComplement ( updatedSociety.getDirectorAddressDetailsComplement ( ) );
            society.get().setDirectorPostalCode ( updatedSociety.getDirectorPostalCode ( ) );
            society.get().setDirectorCity ( updatedSociety.getDirectorCity ( ) );
            society.get().setDirectorCountry ( updatedSociety.getDirectorCountry ( ) );

            societyRepository.save ( society.get() ); // Enregistrer les modifications dans la base de données

            return ResponseEntity.ok ( society );
        }

        return ResponseEntity.status ( HttpStatus.NOT_FOUND ).build ( );
    }
}