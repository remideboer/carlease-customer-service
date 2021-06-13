package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Address;
import com.monolithical.customerservice.domain.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Full integration test */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@SqlGroup({
  @Sql({"/schema.sql"}),
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:test-data.sql")
})
@ActiveProfiles("test") // database is referentie naar h2 in memory modus en modus MySQL
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
public class CustomerResourceITDBTest {

  Logger logger = LoggerFactory.getLogger(CustomerResourceITDBTest.class);

  /** basic integration CRUD tests to be performed: READ - ALL, 1 POST - 1 UPDATE - 1 DELETE - 1 */
  @Autowired private MockMvc mvc;

  @Autowired private JdbcTemplate jdbcTemplate;

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

  @Test
  void post_creates_user_in_datastore_and_results_in_status_201_created() throws Exception {
    // combines a few tests which are best separated:
    // correct location header, status code 201, insert in db, id in location header and db id must
    // every test inserts 2 users by default, this is going to conflict with the auto increment, so
    // clear the table klant in this test
    jdbcTemplate.update("truncate table klant");

    Customer customerResult =
        new Customer.Builder()
            .firstName("fname")
            .lastName("lname")
            .email("post@test.nl")
            .phoneNumber("0101234567")
            .setAddress(
                new Address.Builder()
                    .street("teststraat")
                    .number(123)
                    .postalCode("1234AB")
                    .city("teststad")
                    .build())
            .build();
    customerResult.setId(1L);

    String json =
        "{\"firstName\":\"fname\",\"lastName\":\"lname\",\"lastNamePrefix\":null,\"phoneNumber\":\"0101234567\",\"email\":\"post@test.nl\",\"street\":\"teststraat\",\"postalCode\":\"1234AB\",\"houseNumber\":123,\"addition\":null,\"city\":\"teststad\"}";
    // create request
    mvc.perform(
            post("/customers")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json)
                .with(
                    req -> {
                      req.setServerName("test");
                      return req;
                    }))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(
            header().string("Location", matchesPattern("http://test/customers/[1-9]?[0-9]+")));
    // see if user has been submitted to datastore
    String queryExist =
        "select exists(select 1 from klant where voornaam='fname' and achternaam='lname' and tussenvoegsel is null and straat_naam='teststraat' and postcode='1234AB' and huisnummer=123 and woonplaats='teststad' and emailadres='post@test.nl' and telefoonnummer='0101234567')";
    boolean result = jdbcTemplate.queryForObject(queryExist, Boolean.class);
    assertTrue(result);
  }

  @Test
  void post_creates_user_in_datastore_location_id_matches_db_id() throws Exception {
    // the location header should result in: http://localhost/customers/:id
    // every test inserts 2 users by default, this is going to conflict with the auto increment, so
    // clear the table klant in this test
    jdbcTemplate.update("truncate table klant");

    String json =
        "{\"firstName\":\"fname\",\"lastName\":\"lname\",\"lastNamePrefix\":null,\"phoneNumber\":\"0101234567\",\"email\":\"post@test.nl\",\"street\":\"teststraat\",\"postalCode\":\"1234AB\",\"houseNumber\":123,\"addition\":null,\"city\":\"teststad\"}";
    // run below multiple times to catch hardcoded id's
    // create request
    for (int i = 0; i < 10; i++) {
      var locationHeader =
          mvc.perform(
                  post("/customers")
                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                      .content(json)
                      .with(
                          req -> {
                            req.setServerName("test");
                            return req;
                          }))
              .andReturn()
              .getResponse()
              .getHeader("Location");
      // test if generated db id equals location id
      // parse id from header
      assert locationHeader != null;
      Long headerId = Long.valueOf(locationHeader.split("http://test/customers/")[1]);
      // get last inserted id from db
      Long dbId = jdbcTemplate.queryForObject("SELECT IDENTITY()", Long.class);
      assertThat(headerId, is(dbId));
    }
  }
}
