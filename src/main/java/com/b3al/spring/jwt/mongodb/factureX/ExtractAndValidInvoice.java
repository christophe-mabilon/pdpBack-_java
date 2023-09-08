package com.b3al.spring.jwt.mongodb.factureX;

import com.b3al.spring.jwt.mongodb.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.b3al.spring.jwt.mongodb.models.Invoice;
import com.b3al.spring.jwt.mongodb.models.User;
import com.b3al.spring.jwt.mongodb.repository.UserRepository;
import com.b3al.spring.jwt.mongodb.service.InvoiceService;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class ExtractAndValidInvoice {
    private static final InvoiceService invoiceService =new InvoiceService ();
    private static UserRepository userRepository ;

    public ExtractAndValidInvoice(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    public static  void extract ( MultipartFile file, String userName, String factureId ) throws Exception {
        //Injection de dependances invoiceApiService et invoiceDTO
        Optional<User> user = userRepository.findByUsername ( userName );
        Import anImport = new Import ( file );
        invoiceService.invoiceCreator (anImport,user, factureId );


        Invoice invoiceSelected = invoiceService.getSelectedInvoice ( );

        // Create ObjectMapper for JSON
        ObjectMapper jsonMapper = new ObjectMapper ( );
        jsonMapper.enable ( SerializationFeature.INDENT_OUTPUT );

        // Convert Java object to JSON
        String jsonOutput = jsonMapper.writeValueAsString ( invoiceSelected );
        invoiceService.setSelectedInvoiceJson ( jsonOutput );

    }
}
