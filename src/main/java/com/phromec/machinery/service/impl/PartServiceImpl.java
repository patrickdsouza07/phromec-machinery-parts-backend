package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.part.PartListResponse;
import com.phromec.machinery.dto.part.PartResponse;
import com.phromec.machinery.dto.part.PartSummaryProjection;
import com.phromec.machinery.dto.part.PartSummaryResponse;
import com.phromec.machinery.model.Material;
import com.phromec.machinery.model.part.Part;
import com.phromec.machinery.model.part.PartStatus;
import com.phromec.machinery.model.part.PartVariant;
import com.phromec.machinery.model.part.PartVariantMaterial;
import com.phromec.machinery.repository.PartRepository;
import com.phromec.machinery.service.PartService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PartServiceImpl implements PartService {

    private final PartRepository partRepository;

    private static final Map<String, String> SORT_FIELDS =
            Map.of(
                    "partCode", "partCode",
                    "partName", "partName",
                    "unitOfMeasure", "unitOfMeasure",
                    "status", "status",
                    "createdAt", "createdAt",
                    "updatedAt", "updatedAt"
            );

    @Override
    @Transactional(readOnly = true)
    public PartListResponse getParts(
            int page,
            int size,
            String search,
            PartStatus status,
            String sortBy,
            String direction
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;

        if (search != null) {
            search = search.trim();
            if (search.isEmpty()) {
                search = null;
            }
        }

        String sortProperty = resolveSortProperty(sortBy);
        Sort.Direction sortDirection = resolveSortDirection(direction);

        Sort sort = Sort.by(sortDirection, sortProperty)
                .and(Sort.by(Sort.Direction.DESC, "partId"));

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Part> partPage = partRepository.searchParts(search, status, pageable);

        List<PartResponse> parts = partPage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PartSummaryResponse summary = buildSummary();

        return PartListResponse.builder()
                .summary(summary)
                .parts(parts)
                .page(partPage.getNumber())
                .size(partPage.getSize())
                .totalElements(partPage.getTotalElements())
                .totalPages(partPage.getTotalPages())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PartResponse getPartById(Integer partId) {
        Part part = partRepository.findPartWithDetails(partId)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + partId));

        return mapToResponse(part);
    }

    @Override
    public Part createPart(Part part) {
        validatePart(part);

        if (partRepository.existsByPartCode(part.getPartCode())) {
            throw new IllegalArgumentException("Part code already exists: " + part.getPartCode());
        }

        if (part.getStatus() == null) {
            part.setStatus(PartStatus.ACTIVE);
        }

        if (part.getUnitOfMeasure() == null || part.getUnitOfMeasure().trim().isEmpty()) {
            part.setUnitOfMeasure("Set");
        }

        return partRepository.save(part);
    }

    @Override
    public Part updatePart(Integer partId, Part partDetails) {
        Part existing = partRepository.findById(partId)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + partId));

        if (partDetails.getPartCode() != null && !partDetails.getPartCode().equals(existing.getPartCode())) {
            if (partRepository.existsByPartCodeAndPartIdNot(partDetails.getPartCode(), partId)) {
                throw new IllegalArgumentException("Part code already exists: " + partDetails.getPartCode());
            }
            existing.setPartCode(partDetails.getPartCode());
        }

        if (partDetails.getPartName() != null) {
            existing.setPartName(partDetails.getPartName());
        }

        if (partDetails.getMachineType() != null) {
            existing.setMachineType(partDetails.getMachineType());
        }

        if (partDetails.getDescription() != null) {
            existing.setDescription(partDetails.getDescription());
        }

        if (partDetails.getUnitOfMeasure() != null) {
            existing.setUnitOfMeasure(partDetails.getUnitOfMeasure());
        }

        if (partDetails.getStatus() != null) {
            existing.setStatus(partDetails.getStatus());
        }

        return partRepository.save(existing);
    }

    @Override
    public void deletePart(Integer partId) {
        if (!partRepository.existsById(partId)) {
            throw new RuntimeException("Part not found with id: " + partId);
        }
        partRepository.deleteById(partId);
    }

    private PartSummaryResponse buildSummary() {
        PartSummaryProjection result = partRepository.getPartSummary(
                PartStatus.ACTIVE,
                PartStatus.INACTIVE
        );

        if (result == null) {
            return PartSummaryResponse.builder()
                    .totalParts(0L)
                    .activeParts(0L)
                    .inactiveParts(0L)
                    .inStockParts(0L)
                    .outOfStockParts(0L)
                    .build();
        }

        return PartSummaryResponse.builder()
                .totalParts(safeLong(result.getTotalParts()))
                .activeParts(safeLong(result.getActive()))
                .inactiveParts(safeLong(result.getInactive()))
                .build();
    }

    private PartResponse mapToResponse(Part part) {
        Integer machineTypeId = null;
        String machineTypeName = null;
        String machineName = null;

        if (part.getMachineType() != null) {
            machineTypeId = Math.toIntExact(part.getMachineType().getMachineTypeId());
            machineTypeName = part.getMachineType().getTypeName();
            machineName = part.getMachineType().getTypeName(); // Or machine model name
        }

        // Resolve Material & Category from Variants if available
        String materials = null;
        String category = null;
        PartVariant defaultVariant = (part.getVariants() != null && !part.getVariants().isEmpty())
                ? part.getVariants().getFirst()
                : null;

        if (defaultVariant != null) {
            category = defaultVariant.getHeadType();
            if (defaultVariant.getMaterials() != null && !defaultVariant.getMaterials().isEmpty()) {
                materials = defaultVariant.getMaterials().stream()
                        .map(PartVariantMaterial::getMaterial)
                        .filter(m -> m != null && m.getMaterialName() != null)
                        .map(Material::getMaterialName)
                        .collect(Collectors.joining(", "));
            }
        }

        return PartResponse.builder()
                .partId(part.getPartId())
                .partCode(part.getPartCode())
                .partName(part.getPartName())
                .leadTime("4-6 weeks lead time") // Default/persisted lead time
                .machineTypeId(machineTypeId)
                .machineName(machineName)
                .machineTypeName(machineTypeName)
                .category(category)
                .material(materials)
                .unitOfMeasure(part.getUnitOfMeasure())
                .basePrice(new BigDecimal("185000.00"))
                .stockStatus("In Stock")
                .status(part.getStatus().getValue())
                .description(part.getDescription())
                .build();
    }

    private void validatePart(Part part) {
        if (part == null) {
            throw new IllegalArgumentException("Part cannot be null");
        }
        if (part.getPartCode() == null || part.getPartCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Part code is required");
        }
        if (part.getPartName() == null || part.getPartName().trim().isEmpty()) {
            throw new IllegalArgumentException("Part name is required");
        }
    }

    private String resolveSortProperty(String sortBy) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "partName";
        }
        return SORT_FIELDS.getOrDefault(sortBy, "partName");
    }

    private Sort.Direction resolveSortDirection(String direction) {
        if ("asc".equalsIgnoreCase(direction)) {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }

    private long safeLong(Long value) {
        return value != null ? value : 0L;
    }
}