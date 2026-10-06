package com.phromec.machinery.controller;

import com.phromec.machinery.dto.material.MaterialListResponse;
import com.phromec.machinery.dto.material.MaterialResponse;
import com.phromec.machinery.model.Material;
import com.phromec.machinery.model.RecordStatus;
import com.phromec.machinery.service.MaterialService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600000)
@RestController
@RequestMapping("/phromecManagement/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    public ResponseEntity<MaterialListResponse> getMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam( defaultValue = "materialName" ) String sortBy,
            @RequestParam( defaultValue = "asc" ) String direction
    ) {

        RecordStatus materialStatus = parseStatus(status);

        return ResponseEntity.ok(
                materialService.getMaterials(
                        page,
                        size,
                        search,
                        materialStatus,
                        sortBy,
                        direction
                )
        );
    }


    @GetMapping("/{materialId}")
    public ResponseEntity<MaterialResponse>
    getMaterial(
            @PathVariable Integer materialId
    ) {

        return ResponseEntity.ok(
                materialService.getMaterialById(materialId)
        );
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(
            @RequestBody Material material
    ) {

        Material created = materialService.createMaterial( material );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{materialId}")
    public ResponseEntity<Material> updateMaterial(
            @PathVariable Integer materialId,
            @RequestBody Material material
    ) {

        return ResponseEntity.ok(
                materialService.updateMaterial(
                        materialId,
                        material
                )
        );
    }


    @DeleteMapping("/{materialId}")
    public ResponseEntity<Void> deleteMaterial(
            @PathVariable Integer materialId
    ) {

        materialService.deleteMaterial(materialId);

        return ResponseEntity
                .noContent()
                .build();
    }

    private RecordStatus parseStatus(
            String status
    ) {

        if (status == null
                || status.trim().isEmpty()
                || "ALL".equalsIgnoreCase(status)) {

            return null;
        }

        for (RecordStatus value :
                RecordStatus.values()) {

            if (value.name()
                    .equalsIgnoreCase(status)
                    ||
                    value.getValue()
                            .equalsIgnoreCase(status)) {

                return value;
            }
        }

        throw new IllegalArgumentException(
                "Invalid material status: "
                        + status
        );
    }
}