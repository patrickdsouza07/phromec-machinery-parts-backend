package com.phromec.machinery.dto.inventory;

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
public class InventoryListResponse {

    private InventorySummaryResponse summary;

    private List<InventoryItemResponse> inventory;

    private int page;
    private int size;

    private long totalElements;
    private int totalPages;
}