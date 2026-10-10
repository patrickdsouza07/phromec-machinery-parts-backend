package com.phromec.machinery.dto.customer;

import com.phromec.machinery.model.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String city;
    private String state;
    private String gstNumber;
    private String panNumber;
    private Customer.Status status;
    private String notes;
}
