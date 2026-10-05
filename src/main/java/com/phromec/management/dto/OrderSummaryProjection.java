package com.phromec.management.dto;

import java.math.BigDecimal;

public interface OrderSummaryProjection {

    Long getTotalOrders();

    BigDecimal getTotalValue();

    Long getProcessing();

    Long getConfirmed();

    Long getInProduction();

    Long getShipped();

    Long getCompleted();
}
