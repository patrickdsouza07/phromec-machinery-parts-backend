package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.machine.MachineTypeCountProjection;
import com.phromec.machinery.dto.machine.MachineTypeListItemResponse;
import com.phromec.machinery.dto.machine.CreateMachineTypeRequest;
import com.phromec.machinery.model.machine.MachineType;
import com.phromec.machinery.repository.MachineTypeRepository;
import com.phromec.machinery.repository.MachineRepository;
import com.phromec.machinery.repository.PartRepository;
import com.phromec.machinery.service.MachineTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MachineTypeServiceImpl implements MachineTypeService {

    private final MachineTypeRepository machineTypeRepository;
    private final MachineRepository machineRepository;
    private final PartRepository partRepository;

    @Override
    public java.util.List<MachineTypeListItemResponse> getMachineTypes() {
        Map<Integer, Long> machineCounts = toCountMap(machineRepository.countMachinesByType());
        Map<Integer, Long> partCounts = toCountMap(partRepository.countPartsByType());

        return machineTypeRepository.findAllByOrderByTypeNameAsc().stream()
                .map(type -> mapToResponse(type, machineCounts, partCounts))
                .toList();
    }

    @Override
    public java.util.List<String> getMachineTypeNames() {
        return machineTypeRepository.findAllByOrderByTypeNameAsc().stream()
                .map(MachineType::getTypeName)
                .toList();
    }

    @Override
    @Transactional
    public MachineType createMachineType(CreateMachineTypeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Machine type request cannot be null");
        }

        String typeName = request.getTypeName() == null ? null : request.getTypeName().trim();
        String typeCode = request.getTypeCode() == null ? null : request.getTypeCode().trim();
        if (typeName == null || typeName.isEmpty()) {
            throw new IllegalArgumentException("Machine type name is required");
        }
        if (typeCode == null || typeCode.isEmpty()) {
            throw new IllegalArgumentException("Machine type code is required");
        }
        if (machineTypeRepository.existsByTypeNameIgnoreCase(typeName)) {
            throw new IllegalArgumentException("Machine type name already exists: " + typeName);
        }
        if (machineTypeRepository.existsByTypeCodeIgnoreCase(typeCode)) {
            throw new IllegalArgumentException("Machine type code already exists: " + typeCode);
        }

        MachineType machineType = new MachineType();
        machineType.setTypeName(typeName);
        machineType.setTypeCode(typeCode);
        machineType.setDescription(request.getDescription());
        return machineTypeRepository.save(machineType);
    }

    @Override
    @Transactional
    public void deleteMachineType(Integer machineTypeId) {
        if (!machineTypeRepository.existsById(machineTypeId)) {
            throw new RuntimeException("Machine type not found with id: " + machineTypeId);
        }
        machineTypeRepository.deleteById(machineTypeId);
    }

    private Map<Integer, Long> toCountMap(java.util.List<MachineTypeCountProjection> counts) {
        return counts.stream().collect(Collectors.toMap(
                MachineTypeCountProjection::getMachineTypeId,
                projection -> projection.getCount() == null ? 0L : projection.getCount()
        ));
    }

    private MachineTypeListItemResponse mapToResponse(
            MachineType type, Map<Integer, Long> machineCounts, Map<Integer, Long> partCounts) {
        Integer id = type.getMachineTypeId();
        return MachineTypeListItemResponse.builder()
                .machineTypeId(id)
                .typeCode(type.getTypeCode())
                .typeName(type.getTypeName())
                .description(type.getDescription())
                .status(type.getStatus())
                .machineCount(machineCounts.getOrDefault(id, 0L))
                .partsCount(partCounts.getOrDefault(id, 0L))
                .createdAt(type.getCreatedAt())
                .build();
    }
}
