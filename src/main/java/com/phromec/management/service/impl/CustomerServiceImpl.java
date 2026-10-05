package com.phromec.management.service.impl;

import com.phromec.management.model.Customer;
import com.phromec.management.repository.CustomerRepository;
import com.phromec.management.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
