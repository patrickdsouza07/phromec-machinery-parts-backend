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
public class MaterialResponse {

    private Integer materialId;

    private String materialCode;

    private String materialName;

    private String description;

    private String unit;

    private String status;
}