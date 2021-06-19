package com.monolithical.customerservice.util.validation;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JWTValidatorTest {

  private String secret;
  private String validJWT;

  @BeforeEach
  private void init() throws JOSEException {
    secret = "imasecretimasecretimasecretimasecret";
    JWSSigner signer = new MACSigner(secret);

    JWTClaimsSet claimsSet =
        new JWTClaimsSet.Builder()
            .expirationTime(
                new Date(new Date().getTime() + 60 * 1000)) // expiration in seconds to millis
            .build();
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(signer);
    validJWT = signedJWT.serialize();
  }

  @Test
  void isValid_correct_jwt_returns_true() {
    // alg HS256, typ, JWT, secret: imasecretimasecretimasecretimasecret
    var jwtValidator = new JWTValidator(secret);

    assertTrue(jwtValidator.isValid(validJWT));
  }

  @Test
  void isValid_compromised_jwt_returns_false() {
    // alg HS256, typ, JWT, secret: imasecretimasecretimasecretimasecret
    String jwt =
        "dyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.HqXboI3jgtnSoM4cT_WaWDTgQ83wMaJVL2X0pdUEvaU";
    var jwtValidator = new JWTValidator(secret);

    assertFalse(jwtValidator.isValid(jwt));
  }

  @Test
  void isValid_expired_JWT_returns_false() throws JOSEException {
    var jwtValidator = new JWTValidator(secret);
    JWSSigner signer = new MACSigner(secret);

    JWTClaimsSet claimsSet =
        new JWTClaimsSet.Builder()
            .expirationTime(
                new Date(
                    ZonedDateTime.now()
                        .minusHours(4)
                        .toEpochSecond())) // setting expiration to before now
            .build();
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(signer);
    String expiredJWT = signedJWT.serialize();

    assertFalse(jwtValidator.isValid(expiredJWT));
  }
}
