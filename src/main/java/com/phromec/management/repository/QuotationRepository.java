package com.phromec.management.repository;

import com.phromec.management.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Integer> {}
