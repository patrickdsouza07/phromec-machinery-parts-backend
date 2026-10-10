package com.phromec.machinery.repository;

import com.phromec.machinery.model.part.PartStatus;
import com.phromec.machinery.model.part.PartVariant;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartVariantRepository extends JpaRepository<PartVariant, Integer> {
    List<PartVariant> findByPart_StatusOrderByPart_PartNameAsc(
            PartStatus status
    );

    Optional<PartVariant> findById(@NonNull Integer variantId);
}
