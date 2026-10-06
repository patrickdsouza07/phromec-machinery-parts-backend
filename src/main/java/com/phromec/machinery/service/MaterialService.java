package com.phromec.machinery.service;

import com.phromec.machinery.dto.material.MaterialListResponse;
import com.phromec.machinery.dto.material.MaterialResponse;
import com.phromec.machinery.model.Material;
import com.phromec.machinery.model.RecordStatus;

public interface MaterialService {

    MaterialListResponse getMaterials(
            int page,
            int size,
            String search,
            RecordStatus status,
            String sortBy,
            String direction
    );

    MaterialResponse getMaterialById(
            Integer materialId
    );

    Material createMaterial(
            Material material
    );

    Material updateMaterial(
            Integer materialId,
            Material material
    );

    void deleteMaterial(
            Integer materialId
    );
}