package com.phromec.machinery.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemResponse {

    private Integer inventoryId;
    private Integer variantId;

    private String partName;
    private String partCode;
    private String variantCode;

    private String machineTypeName;

    private BigDecimal available;
    private BigDecimal reserved;
    private BigDecimal minStock;

    private String unit;
    private String stockStatus;

    private LocalDateTime lastUpdated;
}