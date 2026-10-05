package com.phromec.management.repository;

import com.phromec.management.model.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {}
