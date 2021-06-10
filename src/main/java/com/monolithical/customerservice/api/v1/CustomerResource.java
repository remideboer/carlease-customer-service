package com.monolithical.customerservice.api.v1;

import com.monolithical.customerservice.domain.Customer;
import com.monolithical.customerservice.persistence.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerResource {

  private final CustomerRepository customerRepository;

  public CustomerResource(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @GetMapping
  public List<Customer> list() {
    return customerRepository.findAll();
  }
}