package com.phromec.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_code", nullable = false, unique = true, length = 30)
    private String customerCode;

    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country = "India";

    @Column(name = "pincode", length = 20)
    private String pinCode;

    @Column(name = "gst_number", length = 30)
    private String gstNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10)
    private Status status = Status.Active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
	
	@OneToMany(mappedBy="customer") 
	private List<Quotation> quotations=new ArrayList<>();
	
 	@OneToMany(mappedBy="customer") 
	private List<Order> orders=new ArrayList<>();

    public enum Status {
        Active,
        Inactive
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (country == null) {
            country = "India";
        }

        if (status == null) {
            status = Status.Active;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}