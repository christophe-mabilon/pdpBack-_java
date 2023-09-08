package com.b3al.spring.jwt.mongodb.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
  @Id
  private String id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @NotBlank
  @Size(max = 64)
  private String password;

  private Date creationDate;

  private Date updateDate;

  private Boolean isParticular;

  @DBRef
  private Particular particular;

  @DBRef
  private Society society;

  @DBRef
  private Set<Role> roles = new HashSet<> ( );

  private Set<InvoiceFile> invoiceFiles = new HashSet<> ( );

  public User() {
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  public String getEmail ( String email ) {
    return this.email;
  }

}
