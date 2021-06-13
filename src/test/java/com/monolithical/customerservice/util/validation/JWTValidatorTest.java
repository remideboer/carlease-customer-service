package com.monolithical.customerservice.util.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTValidatorTest {

  @Test
  void isValid_correct_jwt_returns_true() {
    // alg HS256, typ, JWT, secret: imasecretimasecretimasecretimasecret
    String validJwt =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.HqXboI3jgtnSoM4cT_WaWDTgQ83wMaJVL2X0pdUEvaU";
    String secret = "imasecretimasecretimasecretimasecret";
    var jwtValidator = new JWTValidator(secret);

    assertTrue(jwtValidator.isValid(validJwt));
  }

  @Test
  void isValid_compromised_jwt_returns_false() {
    // alg HS256, typ, JWT, secret: imasecretimasecretimasecretimasecret
    String validJwt =
        "dyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.HqXboI3jgtnSoM4cT_WaWDTgQ83wMaJVL2X0pdUEvaU";
    String secret = "imasecretimasecretimasecretimasecret";
    var jwtValidator = new JWTValidator(secret);

    assertFalse(jwtValidator.isValid(validJwt));
  }
}
