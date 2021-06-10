package com.monolithical.customerservice.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/** Full integration test */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql({"/schema.sql", "/test-data.sql"})
@ActiveProfiles("test") // database is referentie naar h2 in memory modus en modus MySQL
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
public class CustomerResourceITDBTest {

  /** basic integration CRUD tests to be performed: READ - ALL, 1 POST - 1 UPDATE - 1 DELETE - 1 */
  @Autowired private MockMvc mvc;

  @Test
  void list_fetches_customer_data_from_db() throws Exception {
    mvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.*", isA(ArrayList.class)))
        // customers could be ordered differently since it is a set from DB, no order is given on id
        // in query yet
        // Customer 01
        .andExpect(jsonPath("$[0].id").value(is(1)))
        .andExpect(jsonPath("$[0].firstName").value(is("test01")))
        .andExpect(jsonPath("$[0].lastNamePrefix").value(is("tussen01")))
        .andExpect(jsonPath("$[0].lastName").value(is("achter01")))
        .andExpect(jsonPath("$[0].phoneNumber").value(is("0611111111")))
        .andExpect(jsonPath("$[0].email").value(is("test01@test.nl")))
        .andExpect(jsonPath("$[0].address.street").value(is("weg01")))
        .andExpect(jsonPath("$[0].address.postalCode").value(is("1111AA")))
        .andExpect(jsonPath("$[0].address.number").value(is(1)))
        .andExpect(jsonPath("$[0].address.addition").value(is(nullValue())))
        .andExpect(jsonPath("$[0].address.city").value(is("stad01")))
        // Customer 02
        .andExpect(jsonPath("$[1].id").value(is(2)))
        .andExpect(jsonPath("$[1].firstName").value(is("test02")))
        .andExpect(jsonPath("$[1].lastNamePrefix").value(is(nullValue())))
        .andExpect(jsonPath("$[1].lastName").value(is("achter02")))
        .andExpect(jsonPath("$[1].phoneNumber").value(is("0622222222")))
        .andExpect(jsonPath("$[1].email").value(is("test02@test.nl")))
        .andExpect(jsonPath("$[1].address.street").value(is("weg02")))
        .andExpect(jsonPath("$[1].address.postalCode").value(is("2222BB")))
        .andExpect(jsonPath("$[1].address.number").value(is(2)))
        .andExpect(jsonPath("$[1].address.addition").value(is("bis")))
        .andExpect(jsonPath("$[1].address.city").value(is("stad02")));
  }
}
