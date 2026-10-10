package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.customer.CustomerListResponse;
import com.phromec.machinery.dto.customer.CustomerRequest;
import com.phromec.machinery.model.Customer;
import com.phromec.machinery.repository.CustomerRepository;
import com.phromec.machinery.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    private static final Map<String, String> SORT_FIELDS = Map.ofEntries(
            Map.entry("customerId", "customerId"),
            Map.entry("customerCode", "customerCode"),
            Map.entry("companyName", "companyName"),
            Map.entry("contactPerson", "contactPerson"),
            Map.entry("email", "email"),
            Map.entry("phone", "phone"),
            Map.entry("city", "city"),
            Map.entry("state", "state"),
            Map.entry("status", "status"),
            Map.entry("createdAt", "createdAt"),
            Map.entry("updatedAt", "updatedAt")
    );

    @Override
    @Transactional(readOnly = true)
    public CustomerListResponse getAllCustomers(int page, int size, String search, String status, String sortBy, String direction) {
        page = Math.max(page, 0);
        size = size <= 0 ? 10 : Math.min(size, 100);
        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) search = null;
        }
        Customer.Status customerStatus = parseStatus(status);
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(sortDirection, SORT_FIELDS.getOrDefault(sortBy, "companyName"))
                .and(Sort.by(Sort.Direction.DESC, "customerId"));
        Page<Customer> customers = customerRepository.searchCustomers(
                search, customerStatus, PageRequest.of(page, size, sort));
        return CustomerListResponse.builder().customers(customers.getContent()).page(customers.getNumber())
                .size(customers.getSize()).totalElements(customers.getTotalElements())
                .totalPages(customers.getTotalPages()).build();
    }

    private Customer.Status parseStatus(String status) {
        if (status == null || status.isBlank() || "ALL".equalsIgnoreCase(status.trim())) {
            return null;
        }
        for (Customer.Status value : Customer.Status.values()) {
            if (value.name().equalsIgnoreCase(status.trim())) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid customer status: " + status);
    }

    @Override
    public Customer createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        apply(customer, request);
        customer.setCustomerCode("CUST-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Integer customerId, CustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
        apply(customer, request);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with id: " + customerId));
        customerRepository.delete(customer);
    }

    private void apply(Customer customer, CustomerRequest request) {
        customer.setCompanyName(request.getCompanyName());
        customer.setContactPerson(request.getContactPerson());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setCity(request.getCity());
        customer.setState(request.getState());
        customer.setGstNumber(request.getGstNumber());
        customer.setPanNumber(request.getPanNumber());
        customer.setStatus(request.getStatus() == null ? Customer.Status.Active : request.getStatus());
        customer.setNotes(request.getNotes());
    }
}
