package com.monolithical.customerservice.api;

import com.monolithical.customerservice.domain.Address;
import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.persistence.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerResource {

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
}
