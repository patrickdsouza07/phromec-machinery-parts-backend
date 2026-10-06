package com.phromec.machinery.repository;

import com.phromec.machinery.model.part.PartVariantMaterial;
import com.phromec.machinery.model.part.PartVariantMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartVariantMaterialRepository extends JpaRepository<PartVariantMaterial, PartVariantMaterialId> {}
