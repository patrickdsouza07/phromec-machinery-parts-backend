package com.phromec.machinery.dto.order;

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
