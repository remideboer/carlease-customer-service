package com.monolithical.customerservice.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {
  @Column(name = "straat_naam")
  private String street;

  @Column(name = "postcode")
  private String postalCode;

  @Column(name = "huisnummer")
  private int number;

  @Column(name = "toevoeging")
  private String addition;

  @Column(name = "woonplaats")
  private String city;

  public Address() {}

  public Address(String street, String postalCode, int number, String addition, String city) {
    this.street = street;
    this.setPostalCode(postalCode);
    this.number = number;
    this.addition = addition;
    this.city = city;
  }

  public void setPostalCode(String postalCode) {
    // simple validation: postal code must start with 1 contain 3 additional digits 0-9 and two
    // characters, treating space between numbers and characters as invalid
    if (!postalCode.matches("[1-9][0-9]{3}[a-zA-Z]{2}")) {
      throw new IllegalArgumentException(
          String.format("incorrect postal code format for: %s", postalCode));
    }
    this.postalCode = postalCode;
  }
}
