package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.persistence.CustomerRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test the JWT authentication on endpoints. */
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerResourceITJWTFilterTest {
  private String validJWT;

  @Autowired private MockMvc mvc;

  @MockBean private CustomerRepository customerRepository;

  @BeforeEach
  private void init() throws JOSEException {
    String secret = "imasecretimasecretimasecretimasecret";
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
  public void get_customers_unauthenticated_returns_401() throws Exception {
    Mockito.when(customerRepository.findAll()).thenReturn(List.of(new Customer()));

    mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void post_customers_unauthenticated_returns_401() throws Exception {
    Mockito.when(customerRepository.findAll()).thenReturn(List.of(new Customer()));

    mvc.perform(post("/customers").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void get_customers_unauthenticated_returns_200() throws Exception {
    Mockito.when(customerRepository.findAll()).thenReturn(List.of(new Customer()));

    mvc.perform(
            get("/customers")
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + validJWT))
        .andExpect(status().isOk());
  }
}
