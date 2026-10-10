package com.phromec.machinery.repository;

import com.phromec.machinery.model.PricingConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PricingConfigurationRepository
        extends JpaRepository<PricingConfiguration, Integer> {

    Optional<PricingConfiguration> findByVariant_VariantId(
            Integer variantId
    );
}
