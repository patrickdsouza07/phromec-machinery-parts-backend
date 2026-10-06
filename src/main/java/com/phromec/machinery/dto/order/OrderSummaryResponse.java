package com.phromec.machinery.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponse {

    private Long totalOrders;

    private BigDecimal totalValue;

    private Long processing;

    private Long confirmed;

    private Long inProduction;

    private Long shipped;

    private Long completed;
}