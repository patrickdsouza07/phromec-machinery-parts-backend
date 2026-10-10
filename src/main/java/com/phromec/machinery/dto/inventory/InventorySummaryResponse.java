package com.phromec.machinery.dto.inventory;

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
public class InventorySummaryResponse {

    private long totalParts;
    private long inStock;
    private long lowStock;
    private long outOfStock;
}
