package com.monolithical.customerservice.domain;

import com.monolithical.customerservice.domain.Address;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

  @Test
  void setPostalCode_invalid_throws_exception_with_message() {
    String[] invalidValues = {"0123AD", "1233", "122", "0900AD"}; // clustering varied invalid types
    for (String value : invalidValues) {
      var ex =
          assertThrows(
              IllegalArgumentException.class,
              () -> {
                new Address().setPostalCode(value);
              });
      assertThat(
          ex.getMessage(), equalTo(String.format("incorrect postal code format for: %s", value)));
    }
  }

  @Test
  void setPostalCode_valid_input_does_not_throw_exception() {
    String[] validValues = {"3522PL", "1788ML", "1349DE", "1349de"};
    assertDoesNotThrow(
        () -> {
          for (String value : validValues) {
            new Address().setPostalCode(value);
          }
        });
  }
}
