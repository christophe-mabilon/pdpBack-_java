package com.b3al.spring.jwt.mongodb.controllers;

import com.b3al.spring.jwt.mongodb.models.InvoiceFile;
import com.b3al.spring.jwt.mongodb.models.User;
import com.b3al.spring.jwt.mongodb.payload.response.ResponseWithId;
import com.b3al.spring.jwt.mongodb.repository.InvoiceFileRepository;
import com.b3al.spring.jwt.mongodb.service.InvoiceService;
import com.b3al.spring.jwt.mongodb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/invoice/file")
public class InvoiceFileController {

    @Autowired
    private UserService userService;
    @Autowired
    private final InvoiceFileRepository invoiceFileRepository;
    @Autowired
    private final InvoiceService invoiceService;
    @Autowired
    public InvoiceFileController ( InvoiceFileRepository invoiceFileRepository , InvoiceService invoiceService ) {
        this.invoiceFileRepository = invoiceFileRepository;
        this.invoiceService = invoiceService;
    }

    // Authentication depuis le contexte de sécurité
    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<InvoiceFile>> getAllInvoices() {
        List<InvoiceFile> invoiceFiles = invoiceFileRepository.findAll();
        if (invoiceFiles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok( invoiceFiles );
    }




    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getInvoiceById(@PathVariable("id") String id) {
        Optional<InvoiceFile> invoiceOptional = invoiceFileRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            InvoiceFile invoiceFile = invoiceOptional.get();
            return ResponseEntity.ok( invoiceFile );
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<?> getInvoiceByUserId(@PathVariable("id") String userId) {
        Optional<InvoiceFile> invoice = invoiceFileRepository.findByUserId(userId);

        if (invoice.isPresent()) {
            invoice.get().setModifiedByUserId(getUserId());
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @PostMapping("/{id}/add")
    public ResponseEntity<?> addInvoice( @RequestBody String invoiceFileRequest, @PathVariable("id") String userName ) throws Exception {
        InvoiceFile invoiceFile = new InvoiceFile ();
        Optional<User> user = userService.findByUsername (userName);
        invoiceFile.setUserId( String.valueOf ( user.get().getId () ) );
        invoiceFile.setModifiedByUserId( String.valueOf ( user.get().getId () ) );
        invoiceFile.setPdfFileB64( invoiceFileRequest);
        invoiceFile.setCreationDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( invoiceFile.getCreationDate());
        calendar.add(Calendar.MONTH, 1); // Date de destruction automatique de la facture
        invoiceFile.setDestructionDate(calendar.getTime());
        invoiceFileRepository.save( invoiceFile );
        String message = "La facture a été ajoutée avec succès !";
        ResponseEntity.ok(message);
        Object test = invoiceService.testAndSaveInvoice ( invoiceFileRequest,user.get().getUsername (), String.valueOf ( invoiceFile.getId() ) );
        return ResponseEntity.ok(test);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable("id") String id, @RequestBody InvoiceFile invoiceFileRequest ) {
        Optional<InvoiceFile> invoiceOptional = invoiceFileRepository.findById(id);
        if (invoiceOptional.isPresent()) {
            InvoiceFile invoiceFile = invoiceOptional.get();
            invoiceFile.setModifiedByUserId(String.valueOf ( getUserId() ));
            invoiceFile.setInvoiceNumber( invoiceFileRequest.getInvoiceNumber());
            invoiceFile.setPdfFileB64( invoiceFileRequest.getPdfFileB64());
            // Construire la réponse avec l'ID de la facture
            String message = "La facture a été modifiée avec succès !";
            invoiceFile.setCreationDate(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime( invoiceFile.getCreationDate());
            calendar.add(Calendar.MONTH, 1); // Date de destruction automatique de la facture
            invoiceFile.setDestructionDate(calendar.getTime());
            invoiceFileRepository.save( invoiceFile );
            ResponseWithId response = new ResponseWithId(message, invoiceFile.getId());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPER-ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        try {
            Long invoiceId = Long.parseLong(id);
            Optional<InvoiceFile> invoiceOptional = invoiceFileRepository.findById( String.valueOf ( invoiceId ) );
            if (invoiceOptional.isPresent()) {
                invoiceFileRepository.delete(invoiceOptional.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<User> user = userService.findByUsername(username);
        return String.valueOf ( user.get().getId());
    }
}
