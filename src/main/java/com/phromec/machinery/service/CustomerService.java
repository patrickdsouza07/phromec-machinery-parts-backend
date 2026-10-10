package com.phromec.machinery.service;

import com.phromec.machinery.dto.customer.CustomerListResponse;
import com.phromec.machinery.dto.customer.CustomerRequest;
import com.phromec.machinery.model.Customer;

public interface CustomerService {
    CustomerListResponse getAllCustomers(int page, int size, String search, String status, String sortBy, String direction);
    Customer createCustomer(CustomerRequest request);
    Customer updateCustomer(Integer customerId, CustomerRequest request);
    void deleteCustomer(Integer customerId);
}
