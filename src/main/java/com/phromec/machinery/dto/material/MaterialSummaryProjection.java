package com.phromec.machinery.dto.material;

public interface MaterialSummaryProjection {

    Long getTotalMaterials();

    Long getActive();

    Long getInactive();
}