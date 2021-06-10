package com.monolithical.customerservice.util.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class PhoneNumberValidatorTest {

    @Test
    public void isValid_invalid_short_input_returns_false(){
        String[] invalidNumbers = {"023", "045689", "02398745"}; // since this is a simple test, using array of values
        for(String n: invalidNumbers){
            assertFalse(PhoneNumberValidator.isValid(n));
        }
    }

    @Test
    public void isValid_invalid_not_starting_with_leading_zero_input_returns_false(){
        String[] invalidNumbers = {"23", "45689", "2398745"};
        for(String n: invalidNumbers){
            assertFalse(PhoneNumberValidator.isValid(n));
        }
    }
}
