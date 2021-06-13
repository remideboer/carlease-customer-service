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

  public Address(Builder builder) {
    this.street = builder.street;
    this.setPostalCode(builder.postalCode);
    this.number = builder.number;
    this.addition = builder.addition;
    this.city = builder.city;
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

  /** Convenience Builder */
  public static class Builder {
    private String street;
    private String postalCode;
    private int number;
    private String addition;
    private String city;

    public Builder street(String street) {
      this.street = street;
      return this;
    }

    public Builder postalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    public Builder number(int number) {
      this.number = number;
      return this;
    }

    public Builder addition(String addition) {
      this.addition = addition;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Address build() {
      return new Address(this);
    }
  }
}
