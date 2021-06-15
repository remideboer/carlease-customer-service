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
import java.util.Date;

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
      // verify validity
      if(sJwt.verify(verifier)){
        logger.debug("JWT has valid signature");
        // verify expiration
        var expiration = sJwt.getJWTClaimsSet().getExpirationTime(); // if expiration is not present this will return null
        logger.debug("JWT EXPIRING AT: " + expiration);
        if (expiration == null) {return false;}
        Date now = new Date();
        logger.debug("NOW is: " + now);
        return now.before(expiration); // evaluate if the given expiration date is before now
      } else {
        logger.debug("JWT has invalid signature");
        return false;
      }
    } catch (JOSEException | ParseException e) {
      return false;
    }
  }
}
