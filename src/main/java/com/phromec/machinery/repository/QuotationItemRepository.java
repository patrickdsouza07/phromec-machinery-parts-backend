package com.phromec.machinery.repository;

import com.phromec.machinery.model.quotation.QuotationItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {}
