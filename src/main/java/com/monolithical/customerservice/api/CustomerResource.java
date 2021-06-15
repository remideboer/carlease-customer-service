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

  /**
   * Creates Customer resource
   * @param customerRequestBody
   * @param uriComponentsBuilder
   * @return http status 201 if successfully created
   */
  @PostMapping
  ResponseEntity<?> post(
      @Valid @RequestBody CustomerRequestBody customerRequestBody,
      UriComponentsBuilder uriComponentsBuilder) {
    var createdCustomer =
        customerRepository.save(
            new Customer.Builder()
                .firstName(customerRequestBody.getFirstName())
                .lastNamePrefix(customerRequestBody.getLastNamePrefix())
                .lastName(customerRequestBody.getLastName())
                .email(customerRequestBody.getEmail())
                .phoneNumber(customerRequestBody.getPhoneNumber())
                .setAddress(
                    new Address.Builder()
                        .street(customerRequestBody.getStreet())
                        .number(customerRequestBody.getHouseNumber())
                        .addition(customerRequestBody.getAddition())
                        .postalCode(customerRequestBody.getPostalCode())
                        .city(customerRequestBody.getCity())
                        .build())
                .build());
    UriComponents uriComponents =
        uriComponentsBuilder.path("customers/{id}").buildAndExpand(createdCustomer.getId());
    return ResponseEntity.created(uriComponents.toUri()).build();
  }

  /**
   * Fetches resources by given id
   * @param id
   * @return Customer resources with status code 200 OK if found
   * status 404 Not Found is resources is not present
   */
  @GetMapping("/{id}")
  ResponseEntity<?> fetchById(@PathVariable("id") Long id) {
    var optional = customerRepository.findById(id);
    if (optional.isPresent()) {
      return ResponseEntity.ok(optional.get());
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Updates resource with given id
   * @param id
   * @param customerRequestBody
   * @return status 204 if successfully updated
   * status 404 Not Found if resources is not present
   */
  @PutMapping("/{id}")
  ResponseEntity<?> update(
      @PathVariable("id") Long id, @RequestBody CustomerRequestBody customerRequestBody) {
    var optional = customerRepository.findById(id);
    if (optional.isPresent()) {
      var customer = optional.get();
      customer.setFirstName(customerRequestBody.getFirstName());
      customer.setLastNamePrefix(customerRequestBody.getLastNamePrefix());
      customer.setLastName(customerRequestBody.getLastName());
      customer.setEmail(customerRequestBody.getEmail());
      customer.setPhoneNumber(customerRequestBody.getPhoneNumber());
      customer.setAddress(
          new Address.Builder()
              .street(customerRequestBody.getStreet())
              .addition(customerRequestBody.getAddition())
              .postalCode(customerRequestBody.getPostalCode())
              .city(customerRequestBody.getCity())
              .build());
      customerRepository.save(customer);
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Deletes resources with given id
   *
   * @param id
   * @return status 204 if successfully updated
   * status 404 Not Found if resources is not present
   */
  @DeleteMapping("/{id}")
  ResponseEntity<?> delete(@PathVariable("id") Long id) {
    var optional = customerRepository.findById(id);
    if(optional.isPresent()){
      customerRepository.delete(optional.get());
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}
