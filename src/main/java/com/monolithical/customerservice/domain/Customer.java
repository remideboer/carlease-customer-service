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
  public Customer(Builder builder) {
    this.id = builder.id;
    this.firstName = builder.firstName;
    this.lastNamePrefix = builder.lastNamePrefix;
    this.lastName = builder.lastName;
    this.setPhoneNumber(builder.phoneNumber);
    this.setEmail(builder.email);
    this.address = builder.address;
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

  /** Convenience builder */
  public static class Builder {
    private Long id;
    private String firstName;
    private String lastNamePrefix;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Address address;

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastNamePrefix(String lastNamePrefix) {
      this.lastNamePrefix = lastNamePrefix;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder setAddress(Address address) {
      this.address = address;
      return this;
    }

    public Customer build() {
      return new Customer(this);
    }
  }
}
