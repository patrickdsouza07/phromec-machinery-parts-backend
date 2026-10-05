package com.phromec.management.repository;

import com.phromec.management.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricingRepository extends JpaRepository<Pricing, Integer> {}
