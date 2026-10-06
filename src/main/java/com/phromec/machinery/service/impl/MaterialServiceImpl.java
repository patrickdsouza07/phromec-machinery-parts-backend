package com.phromec.machinery.service.impl;

import com.phromec.machinery.dto.material.MaterialListResponse;
import com.phromec.machinery.dto.material.MaterialResponse;
import com.phromec.machinery.dto.material.MaterialSummaryProjection;
import com.phromec.machinery.dto.material.MaterialSummaryResponse;
import com.phromec.machinery.model.Material;
import com.phromec.machinery.model.RecordStatus;
import com.phromec.machinery.repository.MaterialRepository;
import com.phromec.machinery.service.MaterialService;

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
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;

    private static final Map<String, String> SORT_FIELDS =
            Map.of(
                    "materialCode", "materialCode",
                    "materialName", "materialName",
                    "unit", "unit",
                    "status", "status",
                    "createdAt", "createdAt"
            );


    @Override
    @Transactional(readOnly = true)
    public MaterialListResponse getMaterials(

            int page,

            int size,

            String search,

            RecordStatus status,

            String sortBy,

            String direction
    ) {


        /*
         * Pagination validation
         */

        if (page < 0) {
            page = 0;
        }

        if (size <= 0) {
            size = 10;
        }

        if (size > 100) {
            size = 100;
        }


        /*
         * Search cleanup
         */

        if (search != null) {

            search = search.trim();

            if (search.isEmpty()) {
                search = null;
            }
        }


        /*
         * Sorting
         */

        String sortProperty =
                resolveSortProperty(sortBy);

        Sort.Direction sortDirection =
                resolveSortDirection(direction);


        Sort sort = Sort.by(
                sortDirection,
                sortProperty
        );


        /*
         * Deterministic secondary sorting.
         */

        sort = sort.and(
                Sort.by(
                        Sort.Direction.DESC,
                        "materialId"
                )
        );


        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort
                );


        /*
         * Database query.
         */

        Page<Material> materialPage =
                materialRepository.searchMaterials(
                        search,
                        status,
                        pageable
                );


        /*
         * Map to DTO.
         */

        List<MaterialResponse> materials =
                materialPage
                        .getContent()
                        .stream()
                        .map(this::mapToResponse)
                        .toList();


        /*
         * Summary.
         */

        MaterialSummaryResponse summary =
                buildSummary();


        return MaterialListResponse.builder()
                .summary(summary)
                .materials(materials)
                .page(materialPage.getNumber())
                .size(materialPage.getSize())
                .totalElements(
                        materialPage.getTotalElements()
                )
                .totalPages(
                        materialPage.getTotalPages()
                )
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public MaterialResponse getMaterialById(
            Integer materialId
    ) {

        Material material =
                materialRepository
                        .findById(materialId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Material not found with id: "
                                                + materialId
                                )
                        );

        return mapToResponse(material);
    }


    @Override
    public Material createMaterial(
            Material material
    ) {

        validateMaterial(material);


        if (materialRepository
                .existsByMaterialCode(
                        material.getMaterialCode()
                )) {

            throw new IllegalArgumentException(
                    "Material code already exists: "
                            + material.getMaterialCode()
            );
        }


        if (materialRepository
                .existsByMaterialName(
                        material.getMaterialName()
                )) {

            throw new IllegalArgumentException(
                    "Material name already exists: "
                            + material.getMaterialName()
            );
        }


        if (material.getStatus() == null) {
            material.setStatus(
                    RecordStatus.ACTIVE
            );
        }


        return materialRepository.save(
                material
        );
    }


    @Override
    public Material updateMaterial(
            Integer materialId,
            Material materialDetails
    ) {

        Material existing =
                materialRepository
                        .findById(materialId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Material not found with id: "
                                                + materialId
                                )
                        );


        /*
         * Material code
         */

        if (materialDetails.getMaterialCode() != null
                && !materialDetails
                .getMaterialCode()
                .equals(
                        existing.getMaterialCode()
                )) {


            if (materialRepository
                    .existsByMaterialCodeAndMaterialIdNot(
                            materialDetails
                                    .getMaterialCode(),
                            materialId
                    )) {

                throw new IllegalArgumentException(
                        "Material code already exists: "
                                + materialDetails
                                .getMaterialCode()
                );
            }


            existing.setMaterialCode(
                    materialDetails
                            .getMaterialCode()
            );
        }


        /*
         * Material name
         */

        if (materialDetails.getMaterialName() != null
                && !materialDetails
                .getMaterialName()
                .equals(
                        existing.getMaterialName()
                )) {


            if (materialRepository
                    .existsByMaterialNameAndMaterialIdNot(
                            materialDetails
                                    .getMaterialName(),
                            materialId
                    )) {

                throw new IllegalArgumentException(
                        "Material name already exists: "
                                + materialDetails
                                .getMaterialName()
                );
            }


            existing.setMaterialName(
                    materialDetails
                            .getMaterialName()
            );
        }


        if (materialDetails.getDescription() != null) {

            existing.setDescription(
                    materialDetails
                            .getDescription()
            );
        }


        if (materialDetails.getUnit() != null) {

            existing.setUnit(
                    materialDetails.getUnit()
            );
        }


        if (materialDetails.getStatus() != null) {

            existing.setStatus(
                    materialDetails.getStatus()
            );
        }


        return materialRepository.save(
                existing
        );
    }


    @Override
    public void deleteMaterial(
            Integer materialId
    ) {

        if (!materialRepository
                .existsById(materialId)) {

            throw new RuntimeException(
                    "Material not found with id: "
                            + materialId
            );
        }


        materialRepository.deleteById(
                materialId
        );
    }


    /*
     * =========================================================
     * SUMMARY
     * =========================================================
     */

    private MaterialSummaryResponse buildSummary() {

        MaterialSummaryProjection result =
                materialRepository.getMaterialSummary(

                        RecordStatus.ACTIVE,

                        RecordStatus.INACTIVE
                );


        if (result == null) {

            return MaterialSummaryResponse
                    .builder()
                    .totalMaterials(0L)
                    .active(0L)
                    .inactive(0L)
                    .build();
        }


        return MaterialSummaryResponse
                .builder()
                .totalMaterials(
                        safeLong(
                                result.getTotalMaterials()
                        )
                )
                .active(
                        safeLong(
                                result.getActive()
                        )
                )
                .inactive(
                        safeLong(
                                result.getInactive()
                        )
                )
                .build();
    }


    /*
     * =========================================================
     * ENTITY -> DTO
     * =========================================================
     */

    private MaterialResponse mapToResponse(
            Material material
    ) {

        return MaterialResponse.builder()
                .materialId(
                        material.getMaterialId()
                )
                .materialCode(
                        material.getMaterialCode()
                )
                .materialName(
                        material.getMaterialName()
                )
                .description(
                        material.getDescription()
                )
                .unit(
                        material.getUnit()
                )
                .status(
                        material.getStatus() != null
                                ? material.getStatus().getValue()
                                : null
                )
                .build();
    }


    private void validateMaterial(
            Material material
    ) {

        if (material == null) {

            throw new IllegalArgumentException(
                    "Material cannot be null"
            );
        }


        if (material.getMaterialCode() == null
                || material.getMaterialCode()
                .trim()
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "Material code is required"
            );
        }


        if (material.getMaterialName() == null
                || material.getMaterialName()
                .trim()
                .isEmpty()) {

            throw new IllegalArgumentException(
                    "Material name is required"
            );
        }
    }


    private String resolveSortProperty(
            String sortBy
    ) {

        if (sortBy == null
                || sortBy.trim().isEmpty()) {

            return "materialName";
        }


        return SORT_FIELDS.getOrDefault(
                sortBy,
                "materialName"
        );
    }


    private Sort.Direction resolveSortDirection(
            String direction
    ) {

        if ("asc".equalsIgnoreCase(direction)) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }


    private long safeLong(Long value) {
        return value != null ? value : 0L;
    }
}