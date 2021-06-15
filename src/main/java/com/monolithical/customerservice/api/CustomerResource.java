package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Address;
import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.persistence.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

  private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);
  private final CustomerRepository customerRepository;

  public CustomerResource(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @GetMapping
  public List<Customer> list() {
    return customerRepository.findAll();
  }

  @PostMapping
  ResponseEntity<?> post(
      @Valid @RequestBody CustomerPostBody customerPostBody,
      UriComponentsBuilder uriComponentsBuilder) {
    var createdCustomer =
        customerRepository.save(
            new Customer.Builder()
                .firstName(customerPostBody.getFirstName())
                .lastNamePrefix(customerPostBody.getLastNamePrefix())
                .lastName(customerPostBody.getLastName())
                .email(customerPostBody.getEmail())
                .phoneNumber(customerPostBody.getPhoneNumber())
                .setAddress(
                    new Address.Builder()
                        .street(customerPostBody.getStreet())
                        .number(customerPostBody.getHouseNumber())
                        .addition(customerPostBody.getAddition())
                        .postalCode(customerPostBody.getPostalCode())
                        .city(customerPostBody.getCity())
                        .build())
                .build());
    UriComponents uriComponents =
        uriComponentsBuilder.path("customers/{id}").buildAndExpand(createdCustomer.getId());
    return ResponseEntity.created(uriComponents.toUri()).build();
  }

  @GetMapping("/{id}")
  ResponseEntity<?> fetchById(@PathVariable("id") Long id) {
    var optional = customerRepository.findById(id);
    if (optional.isPresent()) {
      return ResponseEntity.ok(optional.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PutMapping("/{id}")
  ResponseEntity<?> update(
      @PathVariable("id") Long id, @RequestBody CustomerPostBody customerPostBody) {
    var optional = customerRepository.findById(id);
    if (optional.isPresent()) {
      var customer = optional.get();
      customer.setFirstName(customerPostBody.getFirstName());
      customer.setLastNamePrefix(customerPostBody.getLastNamePrefix());
      customer.setLastName(customerPostBody.getLastName());
      customer.setEmail(customerPostBody.getEmail());
      customer.setPhoneNumber(customerPostBody.getPhoneNumber());
      customer.setAddress(
          new Address.Builder()
              .street(customerPostBody.getStreet())
              .addition(customerPostBody.getAddition())
              .postalCode(customerPostBody.getPostalCode())
              .city(customerPostBody.getCity())
              .build());
      customerRepository.save(customer);
      return ResponseEntity.ok(customer); // test if set in db and if in response body
    }
    return ResponseEntity.notFound().build();
  }
}
