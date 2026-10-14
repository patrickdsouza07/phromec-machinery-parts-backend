package com.phromec.machinery.dto.dashboard;

import java.math.BigDecimal;

public record MonthlyQuotationValueResponse(
        String month,
        String yearMonth,
        BigDecimal quotationValue
) {}
