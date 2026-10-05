package com.phromec.management.repository;

import com.phromec.management.model.PartSpecification;
import com.phromec.management.model.PartSpecificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartSpecificationRepository extends JpaRepository<PartSpecification, PartSpecificationId> {}
