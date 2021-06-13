package com.monolithical.customerservice.api;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/** Class for intercepting the body of the customer Post request */
@Data
public class CustomerPostBody {

  @NotNull private String firstName;
  private String lastNamePrefix;
  @NotNull private String lastName;
  @NotNull private String phoneNumber;
  @Email private String email;
  @NotNull private String street;

  @NotNull
  @Pattern(regexp = "[1-9][0-9]{3}[a-zA-Z]{2}")
  private String postalCode;

  @NotNull private int houseNumber;
  private String addition;
  @NotNull private String city;
}
