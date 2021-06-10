package com.monolithical.customerservice.util.validation;

/** Utility class for phone number validation */
public class PhoneNumberValidator {
  public static boolean isValid(String value) {
    // simple test: leading zero, max 10 digits, excluding -
    return value.matches("0[0-9]{9}");
  }
}
