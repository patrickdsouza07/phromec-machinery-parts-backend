package com.phromec.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Integer orderId;

    private String orderNumber;

    private String customerName;

    private String quotationNumber;

    private LocalDate orderDate;

    private LocalDate expectedDeliveryDate;

    private BigDecimal totalAmount;

    private String status;
}