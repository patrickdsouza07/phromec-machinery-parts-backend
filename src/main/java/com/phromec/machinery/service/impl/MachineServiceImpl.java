package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.machine.MachineListItemResponse;
import com.phromec.machinery.dto.machine.MachineListProjection;
import com.phromec.machinery.dto.machine.MachineListResponse;
import com.phromec.machinery.dto.machine.MachineSummaryProjection;
import com.phromec.machinery.dto.machine.MachineSummaryResponse;
import com.phromec.machinery.model.machine.MachineModel;
import com.phromec.machinery.repository.MachineRepository;
import com.phromec.machinery.service.MachineService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    /*
     * Fields mapped to MachineModel entity attributes.
     */
    private static final Map<String, String> SORT_FIELDS =
            Map.of(
                    "machineName", "modelName",
                    "modelName", "modelName",
                    "machineType", "machineType.typeName",
                    "modelNo", "modelCode",
                    "modelCode", "modelCode",
                    "manufacturer", "brandName",
                    "brandName", "brandName",
                    "description", "description",
                    "status", "status",
                    "createdAt", "createdAt"
            );

    // ============================================================
    // LIST
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public MachineListResponse getMachines(
            int page,
            int size,
            String search,
            Integer machineTypeId,
            String status,
            String sortBy,
            String direction
    ) {

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }

        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) {
                search = null;
            }
        }

        if (status != null) {
            status = status.trim();
            if (status.isEmpty() || "ALL".equalsIgnoreCase(status)) {
                status = null;
            }
        }

        String sortField = resolveSortField(sortBy);
        Sort.Direction sortDirection = resolveDirection(direction);

        Sort sort = Sort.by(sortDirection, sortField)
                .and(Sort.by(Sort.Direction.DESC, "machineModelId"));

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<MachineListProjection> machinePage =
                machineRepository.searchMachines(
                        search,
                        machineTypeId,
                        status,
                        pageable
                );

        List<MachineListItemResponse> machines =
                machinePage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();

        MachineSummaryResponse summary = buildSummary();

        return MachineListResponse
                .builder()
                .summary(summary)
                .machines(machines)
                .page(machinePage.getNumber())
                .size(machinePage.getSize())
                .totalElements(machinePage.getTotalElements())
                .totalPages(machinePage.getTotalPages())
                .build();
    }

    // ============================================================
    // GET BY ID
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public MachineModel getMachineById(Integer machineModelId) {
        return machineRepository
                .findMachineWithDetails(machineModelId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Machine model not found with id: " + machineModelId
                        )
                );
    }

    // ============================================================
    // CREATE
    // ============================================================

    @Override
    public MachineModel createMachine(MachineModel machineModel) {
        validateMachine(machineModel);

        if (machineRepository.existsByModelCodeIgnoreCase(machineModel.getModelCode())) {
            throw new IllegalArgumentException(
                    "Model code already exists: " + machineModel.getModelCode()
            );
        }

        if (machineModel.getStatus() == null || machineModel.getStatus().isBlank()) {
            machineModel.setStatus("Active");
        }

        return machineRepository.save(machineModel);
    }

    // ============================================================
    // UPDATE
    // ============================================================

    @Override
    public MachineModel updateMachine(
            Integer machineModelId,
            MachineModel machineDetails
    ) {
        MachineModel existing = machineRepository
                .findById(machineModelId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Machine model not found with id: " + machineModelId
                        )
                );

        if (machineDetails.getModelCode() != null
                && !machineDetails.getModelCode().equalsIgnoreCase(existing.getModelCode())) {

            if (machineRepository.existsByModelCodeIgnoreCaseAndMachineModelIdNot(
                    machineDetails.getModelCode(),
                    machineModelId
            )) {
                throw new IllegalArgumentException(
                        "Model code already exists: " + machineDetails.getModelCode()
                );
            }
            existing.setModelCode(machineDetails.getModelCode());
        }

        if (machineDetails.getModelName() != null) {
            existing.setModelName(machineDetails.getModelName());
        }

        if (machineDetails.getBrandName() != null) {
            existing.setBrandName(machineDetails.getBrandName());
        }

        if (machineDetails.getMachineType() != null) {
            existing.setMachineType(machineDetails.getMachineType());
        }

        if (machineDetails.getDescription() != null) {
            existing.setDescription(machineDetails.getDescription());
        }

        if (machineDetails.getStatus() != null) {
            existing.setStatus(machineDetails.getStatus());
        }

        return machineRepository.save(existing);
    }

    // ============================================================
    // DELETE
    // ============================================================

    @Override
    public void deleteMachine(Integer machineModelId) {
        if (!machineRepository.existsById(machineModelId)) {
            throw new RuntimeException(
                    "Machine model not found with id: " + machineModelId
            );
        }

        machineRepository.deleteById(machineModelId);
    }

    // ============================================================
    // PROJECTION -> DTO
    // ============================================================

    private MachineListItemResponse mapToResponse(
            MachineListProjection projection
    ) {
        return MachineListItemResponse
                .builder()
                .machineName(projection.getMachineName())
                .description(projection.getDescription())
                .machineTypeId(projection.getMachineTypeId())
                .machineTypeCode(projection.getMachineTypeCode())
                .machineTypeName(projection.getMachineTypeName())
                .machineModelId(projection.getMachineModelId())
                .modelNo(projection.getModelNo())
                .modelName(projection.getModelName())
                .manufacturer(projection.getManufacturer())
                .capacity(projection.getCapacity())
                .partsCount(projection.getPartsCount())
                .status(projection.getStatus())
                .build();
    }

    // ============================================================
    // SUMMARY
    // ============================================================

    private MachineSummaryResponse buildSummary() {
        MachineSummaryProjection result = machineRepository.getMachineSummary();

        if (result == null) {
            return MachineSummaryResponse
                    .builder()
                    .totalMachines(0)
                    .totalTypes(0)
                    .activeMachines(0)
                    .inactiveMachines(0)
                    .build();
        }

        long totalTypes = machineRepository.countDistinctMachineTypes();

        return MachineSummaryResponse
                .builder()
                .totalMachines(safeLong(result.getTotalMachines()))
                .totalTypes(totalTypes)
                .activeMachines(safeLong(result.getActiveMachines()))
                .inactiveMachines(safeLong(result.getInactiveMachines()))
                .build();
    }

    // ============================================================
    // VALIDATION
    // ============================================================

    private void validateMachine(MachineModel machineModel) {
        if (machineModel == null) {
            throw new IllegalArgumentException("Machine model cannot be null");
        }

        if (machineModel.getModelCode() == null || machineModel.getModelCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Model code is required");
        }

        if (machineModel.getModelName() == null || machineModel.getModelName().trim().isEmpty()) {
            throw new IllegalArgumentException("Model name is required");
        }

        if (machineModel.getMachineType() == null) {
            throw new IllegalArgumentException("Machine type is required");
        }
    }

    // ============================================================
    // SORT
    // ============================================================

    private String resolveSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "modelName";
        }

        return SORT_FIELDS.getOrDefault(sortBy, "modelName");
    }

    private Sort.Direction resolveDirection(String direction) {
        if ("asc".equalsIgnoreCase(direction)) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }

    private long safeLong(Long value) {
        return value != null ? value : 0L;
    }
}