package com.phromec.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListResponse {

    private OrderSummaryResponse summary;

    private List<OrderResponse> orders;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;
}