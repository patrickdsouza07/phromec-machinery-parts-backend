package com.phromec.machinery.dto.dashboard;

import java.math.BigDecimal;

public record DashboardOverviewResponse(
        long totalCustomers,
        long totalMachines,
        long totalParts,
        BigDecimal quotationValue
) {}
