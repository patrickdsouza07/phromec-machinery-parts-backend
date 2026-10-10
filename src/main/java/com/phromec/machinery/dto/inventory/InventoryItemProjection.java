package com.phromec.machinery.dto.inventory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface InventoryItemProjection {

    Integer getInventoryId();

    Integer getVariantId();

    String getPartName();

    String getPartCode();

    String getVariantCode();

    String getMachineTypeName();

    BigDecimal getQuantityInStock();

    BigDecimal getReservedQuantity();

    BigDecimal getReorderLevel();

    String getUnit();

    LocalDateTime getLastUpdated();
}