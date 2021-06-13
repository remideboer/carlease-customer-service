package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Address;
import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.filters.JWTFilter;
import com.monolithical.customerservice.persistence.CustomerRepository;
import com.monolithical.customerservice.util.validation.JWTValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerResourceITTest {

  @Autowired private MockMvc mvc;

  @MockBean private CustomerRepository customerRepository;

  @MockBean private JWTFilter jwtFilter;

  @MockBean private JWTValidator jwtValidator;

  @BeforeEach
  void setup() {}

  @Test
  public void list_customers_mapping_exists_returns_200_ok() throws Exception {
    mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
  }

  @Test
  public void get_customers_returns_customer_json_objects_in_json_array() throws Exception {
    Mockito.when(customerRepository.findAll()).thenReturn(List.of(new Customer()));

    mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.*", isA(ArrayList.class)))
        .andExpect(jsonPath("$[0]").value(hasKey("id")))
        .andExpect(jsonPath("$[0]").value(hasKey("firstName")))
        .andExpect(jsonPath("$[0]").value(hasKey("lastNamePrefix")))
        .andExpect(jsonPath("$[0]").value(hasKey("lastName")))
        .andExpect(jsonPath("$[0]").value(hasKey("phoneNumber")))
        .andExpect(jsonPath("$[0]").value(hasKey("email")))
        .andExpect(jsonPath("$[0]").value(hasKey("address")));
  }

  @Test
  public void get_customers_returns_customer_has_embedded_address_object() throws Exception {
    Customer customer = new Customer();
    customer.setAddress(new Address());
    Mockito.when(customerRepository.findAll()).thenReturn(List.of(customer));

    mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.*", isA(ArrayList.class)))
        .andExpect(jsonPath("$[0]").value(hasKey("id")))
        .andExpect(jsonPath("$[0]").value(hasKey("firstName")))
        .andExpect(jsonPath("$[0]").value(hasKey("lastNamePrefix")))
        .andExpect(jsonPath("$[0]").value(hasKey("lastName")))
        .andExpect(jsonPath("$[0]").value(hasKey("phoneNumber")))
        .andExpect(jsonPath("$[0]").value(hasKey("email")))
        .andExpect(jsonPath("$[0]").value(hasKey("address")))
        .andExpect(jsonPath("$[0].address").value(hasKey("street")))
        .andExpect(jsonPath("$[0].address").value(hasKey("postalCode")))
        .andExpect(jsonPath("$[0].address").value(hasKey("number")))
        .andExpect(jsonPath("$[0].address").value(hasKey("addition")))
        .andExpect(jsonPath("$[0].address").value(hasKey("city")));
  }

  @Test
  void post_wrongly_formatted_email_field_in_json_results_in_status_400() throws Exception {
    String json =
        "{\"firstName\":\"fname\",\"lastName\":\"lname\",\"lastNamePrefix\":null,\"phoneNumber\":\"0101234567\",\"email\":\"geenemail\",\"street\":\"teststraat\",\"postalCode\":\"1234AB\",\"houseNumber\":123,\"addition\":null,\"city\":\"teststad\"}";

    mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void post_missing_fields_in_json_results_in_status_400() throws Exception {
    String json =
        "{\"lastName\":\"lname\",\"lastNamePrefix\":null,\"phoneNumber\":\"0101234567\",\"email\":\"post@test.nl\",\"street\":\"teststraat\",\"postalCode\":\"1234AB\",\"houseNumber\":123,\"addition\":null,\"city\":\"teststad\"}";

    mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
        .andExpect(status().isBadRequest());
  }

  @Test
  void post_wrong_postal_code_in_json_results_in_status_400() throws Exception {
    // test a few incorrect formats
    String[] invalidValues = {"0123AD", "1233", "122", "0900AD"};
    for (String value : invalidValues) {
      String json =
          String.format(
              "{\"firstName\":\"fname\",\"lastName\":\"lname\",\"lastNamePrefix\":null,\"phoneNumber\":\"0101234567\",\"email\":\"post@test.nl\",\"street\":\"teststraat\",\"postalCode\":\"%s\",\"houseNumber\":123,\"addition\":null,\"city\":\"teststad\"}",
              value);
      mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
          .andExpect(status().isBadRequest());
    }
  }
}
