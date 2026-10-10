package com.phromec.machinery.dto.customer;

import com.phromec.machinery.model.Customer;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class CustomerListResponse {
    private List<Customer> customers;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
