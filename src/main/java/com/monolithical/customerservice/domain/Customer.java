package com.monolithical.customerservice.domain;

import com.monolithical.customerservice.util.validation.PhoneNumberValidator;
import lombok.Data;
import org.apache.commons.validator.routines.EmailValidator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "klant")
public class Customer {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "voornaam")
  private String firstName;

  @Column(name = "tussenvoegsel")
  private String lastNamePrefix;

  @Column(name = "achternaam")
  private String lastName;
  // could refactor phone number and email to embedded contact class
  @Column(name = "telefoonnummer")
  private String phoneNumber;

  @Column(name = "emailadres")
  private String email;

  @Embedded private Address address;

  /** Used for frameworks and libraries: Jackson/Hibernate/Spring IoC */
  public Customer() {}

  /** For manual construction */
  public Customer(
      Long id,
      String firstName,
      String lastNamePrefix,
      String lastName,
      String phoneNumber,
      String email,
      Address address) {
    this.id = id;
    this.firstName = firstName;
    this.lastNamePrefix = lastNamePrefix;
    this.lastName = lastName;
    this.setPhoneNumber(phoneNumber);
    this.setEmail(email);
    this.address = address;
  }

  public void setEmail(String email) {
    if (!EmailValidator.getInstance().isValid(email)) {
      throw new IllegalArgumentException(String.format("incorrect email format for: %s", email));
    }
    this.email = email;
  }

  public void setPhoneNumber(String phoneNumber) {
    // simple validation
    if (!PhoneNumberValidator.isValid(phoneNumber)) {
      throw new IllegalArgumentException(
          String.format("incorrect phone number format for: %s", phoneNumber));
    }
    this.phoneNumber = phoneNumber;
  }
}
