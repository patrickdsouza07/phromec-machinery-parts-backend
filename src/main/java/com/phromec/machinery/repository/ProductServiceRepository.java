package com.phromec.machinery.repository;

import com.phromec.machinery.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductServiceRepository extends JpaRepository<Product, Long> {
}
