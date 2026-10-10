package com.phromec.machinery.service;

import com.phromec.machinery.dto.inventory.InventoryListResponse;

public interface InventoryService {

    InventoryListResponse getInventory(
            int page,
            int size,
            String search,
            String stockStatus,
            String sortBy,
            String direction
    );
}
