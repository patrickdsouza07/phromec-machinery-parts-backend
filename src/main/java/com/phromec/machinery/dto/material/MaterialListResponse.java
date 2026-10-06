package com.phromec.machinery.dto.material;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialListResponse {

    private MaterialSummaryResponse summary;

    private List<MaterialResponse> materials;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}