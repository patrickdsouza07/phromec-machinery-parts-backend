package com.phromec.machinery.dto.part;

public interface PartSummaryProjection {

    Long getTotalParts();

    Long getActive();

    Long getInactive();
}
