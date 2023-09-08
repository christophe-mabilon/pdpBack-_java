package com.b3al.spring.jwt.mongodb.service;

import com.b3al.spring.jwt.mongodb.factureX.ExtractAndValidInvoice;
import com.b3al.spring.jwt.mongodb.factureX.Import;
import com.b3al.spring.jwt.mongodb.models.*;
import com.b3al.spring.jwt.mongodb.repository.AmountRepository;
import com.b3al.spring.jwt.mongodb.repository.ClientRepository;
import com.b3al.spring.jwt.mongodb.repository.InvoiceRepository;
import com.b3al.spring.jwt.mongodb.repository.ProviderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data

@NoArgsConstructor
@Service
public class InvoiceService {

    private static InvoiceRepository invoiceRepository;
    private static ProviderRepository providerRepository;
    private static ClientRepository clientRepository;
    private static AmountRepository amountRepository;

    private Invoice selectedInvoice = new Invoice ( );
    String selectedInvoiceJson = "";


@Autowired
    public InvoiceService (  InvoiceRepository invoiceRepository,
     ProviderRepository providerRepository,
     ClientRepository clientRepository,
     AmountRepository amountRepository) {
        this.invoiceRepository = invoiceRepository;
        this.providerRepository = providerRepository;
        this.clientRepository = clientRepository;
        this.amountRepository = amountRepository;
    }




    public static String formatDate( String inputDate) {
        if (inputDate == null || inputDate.isEmpty() || inputDate.equals("null")) {
            return "";
        } else {
            String outputDate = "";
            SimpleDateFormat format1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");

            try {
                Date date = format1.parse(inputDate);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                outputDate = outputFormat.format(date);
            } catch (ParseException e) {
                try {
                    Date date = format2.parse(inputDate);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    outputDate = outputFormat.format ( date );
                } catch (ParseException ex) {
                    System.out.println ( "Format de date non pris en charge: " + inputDate );
                    ex.printStackTrace ( );
                }
            }

            return outputDate;
        }
    }

    public ResponseEntity<Object[]> testAndSaveInvoice ( String fileBase64 , @NotBlank @Size(max = 20) String userName , String factureId ) throws Exception {
// Convertir la chaîne Base64 en tableau de bytes
        byte[] fileBytes = Base64.getDecoder ( ).decode ( fileBase64 );

        // Créer un objet MultipartFile à partir des bytes
        MultipartFile file = new BASE64DecodedMultipartFile ( fileBytes );

        ExtractAndValidInvoice.extract ( file , userName , factureId );
        List<?> invoiceDataList = checkInvoiceData ( getSelectedInvoice ( ) );

        // Convertir la liste en tableau
        Object[] invoiceDataArray = invoiceDataList.toArray ( );

        return ResponseEntity.ok ( invoiceDataArray );
    }

    public void invoiceCreator ( Import anImport , Optional<User> user , String factureId ) {

        Invoice invoice = new Invoice ( );
        Provider provider = new Provider ( );
        AddressProvider addressProvider = new AddressProvider ( );
        Client client = new Client ( );
        AddressClient addressClient = new AddressClient ( );
        Amount amount = new Amount ( );

        invoice.setId ( null );
        invoice.setUser ( user.get ( ) );
        invoice.setFileId ( factureId );
        invoice.setInvoiceNumber ( anImport.getInvoiceID ( ) );
        invoice.setPurchaseOrder ( anImport.getIssuerAssignedID ( ) );
        invoice.setWorkStartDate ( formatDate ( String.valueOf ( anImport.getDetailedDeliveryPeriodFrom ( ) ) ) );
        invoice.setDeliveryDate ( formatDate ( anImport.getTaxPointDate ( ) ) );

        provider.setName ( anImport.getDeliveryTradePartyName ( ) );
        provider.setSiret ( anImport.getSellerTradePartySiret ( ) );
        provider.setProfessionalMailAddress ( anImport.getSellerTradePartyEmail ( ) );
        provider.setProfessionalPhoneNumber ( anImport.getSellerTradePartyPhoneNumber ( ).replaceAll ( "\\s+" , "" ) );
        provider.setIntracommunityVATnumber ( anImport.getSellerTradePartyTVA ( ) );
        provider.setSubjectToVAT ( !anImport.getSellerTradePartyTVA ( ).isEmpty ( ) );
        addressProvider.setNumber ( anImport.getDeliveryTradePartyName ( ) );//TODO a revoir
        addressProvider.setAddressDetails ( anImport.getSellerTradePartyAddress ( ).getLineOne ( ) );
        addressProvider.setPostalCode ( anImport.getSellerTradePartyAddress ( ).getPostcodeCode ( ) );
        addressProvider.setCity ( anImport.getSellerTradePartyAddress ( ).getCityName ( ) );
        addressProvider.setCountry ( anImport.getSellerTradePartyAddress ( ).getCountryID ( ) );
        provider.setAddress ( addressProvider );

        client.setName ( anImport.getBuyerTradePartyName ( ) );
        client.setSiret ( anImport.getBuyerTradePartyID ( ) );
        client.setProfessionalMailAddress ( anImport.getBuyerTradePartyEmail ( ) );
        client.setProfessionalPhoneNumber ( anImport.getBuyerTradePartyPhoneNumber ( ).replaceAll ( "\\s+" , "" ) );
        client.setIntracommunityVATnumber ( anImport.getBuyertradePartySpecifiedTaxRegistrationID ( ) );
        client.setSubjectToVAT ( !anImport.getBuyertradePartySpecifiedTaxRegistrationID ( ).isEmpty ( ) );
        addressClient.setNumber ( anImport.getBuyerTradePartyName ( ) );//TODO a revoir
        addressClient.setAddressDetails ( anImport.getBuyerTradePartyAddress ( ).getLineOne ( ) );
        addressClient.setPostalCode ( anImport.getBuyerTradePartyAddress ( ).getPostcodeCode ( ) );
        addressClient.setCity ( anImport.getBuyerTradePartyAddress ( ).getCityName ( ) );
        addressClient.setCountry ( anImport.getBuyerTradePartyAddress ( ).getCountryID ( ) );
        client.setAddress ( addressClient );


        amount.setTotalAmountTva ( Float.parseFloat ( anImport.getTaxTotalAmount ( ) ) );
        amount.setTotalAmountHT ( Float.parseFloat ( anImport.getTaxBasisTotalAmount ( ) ) );
        amount.setTotalAmountTTC ( Float.parseFloat ( anImport.getAmount ( ) ) );
        invoice.setValidated ( false );
        invoice.setProvider ( provider );
        invoice.setClient ( client );
        invoice.setAmount ( amount );
        invoice.setDate ( formatDate ( new Date ( ).toString ( ) ) );
        invoice.setPaidAmount ( anImport.getPaidAmount ( ) );
        invoice.setPaymentAdviceReference ( anImport.getForeignReference ( ) );


        clientRepository.save ( invoice.getClient ( ) );

        providerRepository.save ( invoice.getProvider ( ) );
        amountRepository.save ( invoice.getAmount ( ) );


        this.selectedInvoiceJson = saveJson ( invoice );
        invoice.setJsonFormat ( selectedInvoiceJson );
        this.setSelectedInvoice ( invoice );
        invoiceRepository.save ( invoice );


    }

    private String saveJson ( Invoice invoice ) {
        String jsonString = "";
        ObjectMapper objectMapper = new ObjectMapper ( );
        try {
            jsonString = objectMapper.writeValueAsString ( invoice );

        } catch (JsonProcessingException e) {
            e.printStackTrace ( );
        }
        return jsonString;
    }

    ;


    public List<String> checkInvoiceData ( Invoice invoice ) {
        List<String> invoiceChecked = new ArrayList<> ( );
        this.checkAllFieldsPresent ( invoice , invoiceChecked );
        return invoiceChecked;
    }

    private void checkAllFieldsPresent ( Invoice object , List<String> invoiceChecked ) {
        Class<?> clazz = object.getClass ( );
        Field[] declaredFields = clazz.getDeclaredFields ( );

        for (Field field : declaredFields) {
            if (!field.isAccessible ( )) {
                field.setAccessible ( true );  // Make the field accessible if it's private
            }

            try {
                Object fieldValue = field.get ( object );
                if (fieldValue == null || fieldValue.toString ( ).isEmpty ( ) || fieldValue.equals ( "null" )) {
                    if (!field.getName().equals("id") && !field.getName().equals("fileId")) {
                        invoiceChecked.add("Le champ '" + field.getName() + "' est manquant !");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }
}