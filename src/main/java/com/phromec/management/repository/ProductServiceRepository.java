package com.phromec.management.repository;

import com.phromec.management.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductServiceRepository extends JpaRepository<Product, Long> {
}
