package com.phromec.machinery.repository;

import com.phromec.machinery.model.part.PartSpecification;
import com.phromec.machinery.model.part.PartSpecificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartSpecificationRepository extends JpaRepository<PartSpecification, PartSpecificationId> {}
