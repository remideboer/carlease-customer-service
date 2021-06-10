package com.monolithical.customerservice.domain;

import com.monolithical.customerservice.domain.Customer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class CustomerTest {

  @Test
  void setPhoneNumber_invalid_phoneNumber_throws_exception_with_message() {
    // assumption that a dutch phone number is used, then it must consist of 10 digits
    var invalidNumber = "123";
    var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Customer().setPhoneNumber(invalidNumber);
            });
    assertThat(
        ex.getMessage(),
        equalTo(String.format("incorrect phone number format for: %s", invalidNumber)));
  }

  @Test
  void setPhoneNumber_valid_phoneNumbers_should_not_throw_exception() {
    // assumption that a dutch phone number is used, then it must consist of 10 digits
    String[] validNumbers = {"0612345678", "0301234567"};
    Customer c = new Customer();
    assertDoesNotThrow(
        () -> {
          for (String n : validNumbers) {
            c.setPhoneNumber(n);
          }
        });
  }

  @Test
  void setEmail_invalid_email_throws_exception_with_message() {
    var invalidEmail = "ditisgeenemail";
    var ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
              new Customer().setEmail(invalidEmail);
            });
    assertThat(
        ex.getMessage(), equalTo(String.format("incorrect email format for: %s", invalidEmail)));
  }

  @Test
  void setEmail_valid_email_should_not_throw_exception() {
    String[] validEmails = {"test@test.com", "de_veel_langere_mail@test.nl", "12334@test.com"};
    var c = new Customer();
    assertDoesNotThrow(
        () -> {
          for (String s : validEmails) {
            c.setEmail(s);
          }
        });
  }
}
