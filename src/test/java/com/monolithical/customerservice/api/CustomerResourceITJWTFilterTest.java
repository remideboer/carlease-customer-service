package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.persistence.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Test the JWT authentication on endpoints. */
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerResourceITJWTFilterTest {
  private static final String VALID_JWT =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.dyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.k7lZ72D3X6CkPxkKG-SIQtuXLZzLp9UiudUNl0ockAI";

  @Autowired private MockMvc mvc;

  @MockBean private CustomerRepository customerRepository;

  @BeforeEach
  void setup() {}

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
                .header("Authorization", "Bearer " + VALID_JWT))
        .andExpect(status().isOk());
  }
}
