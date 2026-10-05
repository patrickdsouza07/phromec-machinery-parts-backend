package com.phromec.management.controller;

import com.phromec.management.model.*;
import com.phromec.management.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

}

