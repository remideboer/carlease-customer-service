package com.monolithical.customerservice.persistence;

import com.monolithical.customerservice.domain.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

  List<Customer> findAll();
}
