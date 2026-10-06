package com.phromec.machinery.dto.material;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialSummaryResponse {

    private Long totalMaterials;

    private Long active;

    private Long inactive;
}