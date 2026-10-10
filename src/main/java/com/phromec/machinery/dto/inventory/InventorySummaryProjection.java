package com.phromec.machinery.dto.inventory;

public interface InventorySummaryProjection {

    Long getTotalParts();

    Long getInStock();

    Long getLowStock();

    Long getOutOfStock();
}
