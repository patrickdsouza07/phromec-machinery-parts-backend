package com.phromec.machinery.controller;

import com.phromec.machinery.model.*;
import com.phromec.machinery.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping()
    @PreAuthorize("hasAuthority('PRODUCT_VIEW')")
    public List<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

}

