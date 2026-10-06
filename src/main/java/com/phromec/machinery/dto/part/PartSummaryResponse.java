package com.phromec.machinery.dto.part;

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
public class PartSummaryResponse {

    private Long totalParts;
    private Long activeParts;
    private Long inactiveParts;
    private Long inStockParts;
    private Long outOfStockParts;
}