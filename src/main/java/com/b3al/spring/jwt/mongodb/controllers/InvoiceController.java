package com.b3al.spring.jwt.mongodb.controllers;

import com.b3al.spring.jwt.mongodb.factureX.ExtractAndValidInvoice;
import com.b3al.spring.jwt.mongodb.models.BASE64DecodedMultipartFile;
import com.b3al.spring.jwt.mongodb.models.Invoice;
import com.b3al.spring.jwt.mongodb.repository.InvoiceRepository;
import com.b3al.spring.jwt.mongodb.repository.UserRepository;
import com.b3al.spring.jwt.mongodb.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    private static final String MEDIA_TYPE_FORM_DATA = MediaType.MULTIPART_FORM_DATA_VALUE;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;
    private final UserRepository userRepository;

    public InvoiceController ( InvoiceRepository invoiceRepository , InvoiceService invoiceService , ExtractAndValidInvoice extractAndValidInvoice , UserRepository userRepository ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<List<Invoice>> findAll () {
        List<Invoice> invoices = invoiceRepository.findAll ( );
        return ResponseEntity.ok ( invoices );
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Object[]> addInvoice (
            @RequestParam("factureId") String factureId ,
            @RequestParam("fileBase64") String fileBase64 ,
            @RequestParam("userName") String userName
    ) throws Exception {
        // Convertir la chaîne Base64 en tableau de bytes
        byte[] fileBytes = Base64.getDecoder ( ).decode ( fileBase64 );

        // Créer un objet MultipartFile à partir des bytes
        MultipartFile file = new BASE64DecodedMultipartFile ( fileBytes );

        ExtractAndValidInvoice.extract ( file , userName , factureId );
        List<?> invoiceDataList = invoiceService.checkInvoiceData ( invoiceService.getSelectedInvoice ( ) );

        // Convertir la liste en tableau
        Object[] invoiceDataArray = invoiceDataList.toArray ( );

        return ResponseEntity.ok ( invoiceDataArray );
    }


    @GetMapping("/all")
    public ResponseEntity<List<Invoice>> findInvoice () {
        List<Invoice> savedInvoices = invoiceRepository.findAll ( );
        return ResponseEntity.status ( HttpStatus.OK ).body ( savedInvoices );
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Invoice>> findByUserName(@PathVariable String username) {
        List<Invoice> invoices = invoiceRepository.findAll()
                .stream()
                .filter(invoice -> invoice.getUser().getUsername().equals(username))
                .collect(Collectors.toList());

        return ResponseEntity.ok(invoices);
    }

    @PostMapping("/user/{invoiceId}/status")
    public ResponseEntity<Invoice> changeStatus(@PathVariable String invoiceId, @RequestBody Boolean status) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById ( invoiceId );
        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            invoice.setValidated (status);
            invoiceRepository.save(invoice);
            return ResponseEntity.ok(invoice);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice ( @PathVariable Long id , @RequestBody Invoice updatedInvoice ) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById ( String.valueOf ( id ) );
        if (invoiceOptional.isPresent ( )) {
            Invoice invoice = invoiceOptional.get ( );
            // Update the fields of the existing invoice with the values from updatedInvoice
            invoice.setInvoiceNumber ( updatedInvoice.getInvoiceNumber ( ) );
            invoice.setPurchaseOrder ( updatedInvoice.getPurchaseOrder ( ) );
            invoice.setWorkStartDate ( updatedInvoice.getWorkStartDate ( ) );
            invoice.setDeliveryDate ( updatedInvoice.getDeliveryDate ( ) );

            updatedInvoice = invoiceRepository.save ( invoice );
            return ResponseEntity.ok ( updatedInvoice );
        } else {
            return ResponseEntity.notFound ( ).build ( );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        try {
            Optional<Invoice> invoiceOptional = invoiceRepository.findById ( String.valueOf ( id ) ) ;
            if (invoiceOptional.isPresent()) {
                invoiceRepository.delete(invoiceOptional.get());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    }
