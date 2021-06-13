package com.monolithical.customerservice.util.validation;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
public class JWTValidator {
  private byte[] secret;
  private Logger logger = LoggerFactory.getLogger(JWTValidator.class);

  public JWTValidator(@Value("${secret}") String secret) {
    this.secret = secret.getBytes();
    logger.debug("USING SECRET: " + secret);
  }

  public boolean isValid(String jwt) {
    try {
      var sJwt = SignedJWT.parse(jwt);
      JWSVerifier verifier = new MACVerifier(secret);
      return sJwt.verify(verifier);
    } catch (JOSEException | ParseException e) {
      return false;
    }
  }
}
