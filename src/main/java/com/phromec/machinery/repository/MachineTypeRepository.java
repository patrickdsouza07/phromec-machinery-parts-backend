package com.phromec.machinery.repository;

import com.phromec.machinery.model.machine.MachineType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MachineTypeRepository extends JpaRepository<MachineType, Integer> {
    List<MachineType> findAllByOrderByTypeNameAsc();

    boolean existsByTypeCodeIgnoreCase(String typeCode);

    boolean existsByTypeNameIgnoreCase(String typeName);
}
