package com.phromec.management.repository;

import com.phromec.management.model.PartVariantMaterial;
import com.phromec.management.model.PartVariantMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartVariantMaterialRepository extends JpaRepository<PartVariantMaterial, PartVariantMaterialId> {}
