package com.phromec.machinery.repository;

import com.phromec.machinery.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByCustomerCode(String customerCode);

    @Query(value = """
            SELECT c FROM Customer c
            WHERE (:search IS NULL OR :search = ''
                OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.city) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.state) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.gstNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.panNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :search, '%')))
                AND (:status IS NULL OR c.status = :status)
            """,
            countQuery = """
            SELECT COUNT(c) FROM Customer c
            WHERE (:search IS NULL OR :search = ''
                OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.city) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.state) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.gstNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.panNumber) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.customerCode) LIKE LOWER(CONCAT('%', :search, '%')))
                AND (:status IS NULL OR c.status = :status)
            """)
    Page<Customer> searchCustomers(
            @Param("search") String search,
            @Param("status") Customer.Status status,
            Pageable pageable);

}
