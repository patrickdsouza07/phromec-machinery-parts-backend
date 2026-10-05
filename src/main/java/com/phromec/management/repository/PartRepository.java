package com.phromec.management.repository;

import com.phromec.management.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {


}
